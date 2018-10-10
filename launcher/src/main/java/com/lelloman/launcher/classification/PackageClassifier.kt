package com.lelloman.launcher.classification

import com.lelloman.common.utils.TimeProvider
import com.lelloman.launcher.logger.LauncherLoggerFactory
import com.lelloman.launcher.packages.Package
import com.lelloman.launcher.packages.PackagesManager
import com.lelloman.launcher.persistence.ClassifiedIdentifierDao
import com.lelloman.launcher.persistence.PackageLaunchDao
import com.lelloman.launcher.persistence.model.ClassifiedIdentifier
import com.lelloman.launcher.persistence.model.PackageLaunch
import com.lelloman.nn.Network
import com.lelloman.nn.Training
import com.lelloman.nn.activation.Activation
import com.lelloman.nn.dataset.DataSet
import com.lelloman.nn.dataset.DataSet1D
import com.lelloman.nn.layer.DenseLayer
import com.lelloman.nn.layer.GaussianWeightsInitializer
import com.lelloman.nn.layer.InputLayer
import com.lelloman.nn.optimizer.Adagrad
import io.reactivex.Completable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import java.util.*

class PackageClassifier(
    private val packageLaunchDao: PackageLaunchDao,
    private val packagesManager: PackagesManager,
    private val timeProvider: TimeProvider,
    private val timeEncoder: TimeEncoder,
    private val ioScheduler: Scheduler,
    private val classifiedIdentifierDao: ClassifiedIdentifierDao,
    private val launchesEncoderProvider: IdentifierEncoderProvider,
    loggerFactory: LauncherLoggerFactory
) {

    private val logger = loggerFactory.getLogger(PackageClassifier::class.java)

    private val allLaunches: Single<List<PackageLaunch>>
        get() = packageLaunchDao
            .getAll()
            .firstOrError()

    private val random = Random()

    private val randomTimeDisplace get() = ((random.nextDouble() - .5) * 10 * 60 * 1000).toLong()

    fun classifyWithNeuralNet() {
        val unused = Single
            .zip(
                allLaunches,
                packagesManager.installedPackages.firstOrError(),
                BiFunction<List<PackageLaunch>, List<Package>, Pair<List<PackageLaunch>, List<Package>>> { launches, packages ->
                    launches to packages
                }
            )
            .map { (launches, packages) ->
                if (launches.isEmpty()) {
                    return@map packages.map { ClassifiedPackage(it, 0.0) }
                }
                logger.d("classifyWithNeuralNet() provided ${launches.size} launches")
                val identifiers = launches.map { it.identifier() }.plus(packages.map { it.identifier })
                val launchesEncoder = launchesEncoderProvider.provideEncoder(identifiers)
                val (trainingSet, validationSet) = createDataSet(launches, launchesEncoder)
                logger.d("created training (${trainingSet.size}) and validation (${validationSet.size}) dataset")
                val classifier = trainClassifier(
                    trainingSet = trainingSet,
                    validationSet = validationSet,
                    launchesEncoder = launchesEncoder
                )
                val classification = classifier.forwardPass(arrayOf(createEncodedInput()))[0]
                packages.map {
                    val encodedPackage = launchesEncoder.encode(it.identifier)
                    ClassifiedPackage(
                        pkg = it,
                        score = getScoreForPackage(classification, encodedPackage)
                    )
                }
            }
            .flatMapCompletable { classifiedPackages ->
                Completable.fromAction {
                    classifiedIdentifierDao.deleteAll()
                    classifiedIdentifierDao.insert(classifiedPackages.map {
                        ClassifiedIdentifier(
                            id = 0L,
                            identifier = it.pkg.identifier,
                            score = it.score
                        )
                    })
                }
            }
            .subscribeOn(ioScheduler)
            .observeOn(ioScheduler)
            .subscribe()
    }

    private fun trainClassifier(
        trainingSet: DataSet,
        validationSet: DataSet,
        launchesEncoder: IdentifierEncoder
    ): Network {
        val inputSize = trainingSet.inputDimension.second
        val inputLayer = InputLayer(inputSize)
        val outputSize = launchesEncoder.encodedSize
        val hiddenSize = (inputSize + outputSize) / 2
        val hiddenLayer = DenseLayer(
            size = hiddenSize,
            inputLayer = inputLayer,
            activation = Activation.LEAKY_RELU,
            weightsInitializer = GaussianWeightsInitializer()
        )
        val outputLayer = DenseLayer(
            size = outputSize,
            inputLayer = hiddenLayer,
            activation = Activation.SOFTMAX,
            weightsInitializer = GaussianWeightsInitializer()
        )
        val network = Network
            .Builder()
            .addLayer(inputLayer)
            .addLayer(hiddenLayer)
            .addLayer(outputLayer)
            .build()

        val optimizer = Adagrad(0.00001)
        val epochs = 1000
        val startTime = timeProvider.nowUtcMs()
        val callback = object : Training.PrintEpochCallback() {
            override fun shouldEndTraining(trainingLoss: Double, validationLoss: Double): Boolean {
                return timeProvider.nowUtcMs() - startTime > 1000L * 60 || validationLoss < 0.5
            }
        }

        val training = Training(
            network = network,
            trainingSet = trainingSet,
            validationSet = validationSet,
            callback = callback,
            epochs = epochs,
            optimizer = optimizer
        )
        training.perform()
        return network
    }

    private fun getScoreForPackage(classification: DoubleArray, encodedIdentifier: DoubleArray): Double {
        val identifierIndex = encodedIdentifier.indexOfFirst { it > 0.99 }
        return if (identifierIndex < 0 || identifierIndex >= classification.size)
            0.0
        else
            classification[identifierIndex]
    }

    private fun createEncodedInput(utcMs: Long = timeProvider.nowUtcMs()): DoubleArray {
        val time = timeProvider.getTime(utcMs)
        val encodedWeekTime = timeEncoder.encodeWeekTime(time.weekTime)
        val encodedDayTime = timeEncoder.encodeDayTime(time.dayTime)

        return encodedWeekTime.plus(encodedDayTime)
    }

    private fun createDataSet(launches: List<PackageLaunch>, encoder: IdentifierEncoder): Pair<DataSet, DataSet> {
        val rawDataset = launches.map { it.timestampUtc to encoder.encode(it.identifier()) }

        val validationSetElements = mutableListOf<Pair<Long, DoubleArray>>()
            .apply {
                rawDataset.forEach {
                    add(it.first + randomTimeDisplace to it.second)
                    add(it.first + randomTimeDisplace to it.second)
                }
            }
            .map { (timestamp, encodedIdentifier) ->
                createEncodedInput(timestamp) to encodedIdentifier
            }

        val validationSet = DataSet1D
            .Builder(validationSetElements.size)
            .add(validationSetElements::get)
            .build()

        val trainingSetElemetns = mutableListOf<Pair<Long, DoubleArray>>()
            .apply {
                rawDataset.forEach {
                    add(it)
                    add(it.first + randomTimeDisplace to it.second)
                    add(it.first + randomTimeDisplace to it.second)
                }
            }
            .map { (timestamp, encodedIdentifier) ->
                createEncodedInput(timestamp) to encodedIdentifier
            }

        val trainingSet = DataSet1D
            .Builder(trainingSetElemetns.size)
            .add(trainingSetElemetns::get)
            .build()

        return trainingSet to validationSet
    }

    private fun classifyByMostUsed(packages: List<Package>): Single<List<ClassifiedPackage>> = allLaunches
        .map { launches ->
            val launchesMap = mutableMapOf<String, Int>()
            launches.forEach { launch ->
                val currentScore = if (launchesMap.containsKey(launch.identifier())) {
                    launchesMap[launch.identifier()]!!
                } else {
                    0
                }
                launchesMap[launch.identifier()] = currentScore + 1
            }
            packages
                .map { pkg ->
                    val score = if (launchesMap.containsKey(pkg.identifier)) {
                        launchesMap[pkg.identifier]!!.toDouble()
                    } else 0.0
                    ClassifiedPackage(pkg, score)
                }
        }
}

