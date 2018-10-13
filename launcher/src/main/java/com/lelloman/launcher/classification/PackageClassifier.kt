package com.lelloman.launcher.classification

import com.lelloman.common.LLContext
import com.lelloman.common.utils.TimeProvider
import com.lelloman.launcher.logger.LauncherLoggerFactory
import com.lelloman.launcher.logger.ShouldLogToFile
import com.lelloman.launcher.packages.Package
import com.lelloman.launcher.packages.PackagesManager
import com.lelloman.launcher.persistence.ClassifierPersistence
import com.lelloman.launcher.persistence.PersistentClassificationInfo
import com.lelloman.launcher.persistence.db.ClassifiedIdentifierDao
import com.lelloman.launcher.persistence.db.PackageLaunchDao
import com.lelloman.launcher.persistence.db.model.ClassifiedIdentifier
import com.lelloman.launcher.persistence.db.model.PackageLaunch
import com.lelloman.nn.Network
import com.lelloman.nn.dataset.DataSet
import com.lelloman.nn.dataset.DataSet1D
import io.reactivex.Single
import java.util.*

class PackageClassifier(
    llContext: LLContext,
    private val packageLaunchDao: PackageLaunchDao,
    private val packagesManager: PackagesManager,
    private val timeEncoder: TimeEncoder,
    private val classifiedIdentifierDao: ClassifiedIdentifierDao,
    private val classifierPersistence: ClassifierPersistence,
    private val persistentClassificationInfo: PersistentClassificationInfo,
    private val nnFactory: NnFactory,
    loggerFactory: LauncherLoggerFactory
) : ShouldLogToFile {

    private val logger = loggerFactory.getLogger(PackageClassifier::class.java)

    private val allLaunches: Single<List<PackageLaunch>>
        get() = packageLaunchDao
            .getAll()
            .firstOrError()

    private val random = Random()

    private val ioScheduler = llContext.ioScheduler
    private val newThreadScheduler = llContext.newThreadScheduler
    private val timeProvider: TimeProvider = llContext

    private val randomTimeDisplace get() = ((random.nextDouble() - .5) * 10 * 60 * 1000).toLong()

    private fun performClassification(launches: List<PackageLaunch>, packages: List<Package>) = Single
        .fromCallable {
            if (launches.isEmpty()) {
                packages.map { ClassifiedPackage(it, 0.0) }
            } else {
                logger.d("classifyWithNeuralNet() provided ${launches.size} launches")

                val encoder = makeEncoder(
                    launches = launches,
                    packages = packages
                )
                val classifier = getClassifier(
                    launches = launches,
                    encoder = encoder
                )
                val classification = classifier.forwardPass(arrayOf(createEncodedInput()))[0]
                packages.map {
                    val encodedPackage = encoder.encode(it.identifier())
                    ClassifiedPackage(
                        pkg = it,
                        score = getScoreForPackage(classification, encodedPackage)
                    )
                }
            }
        }

    fun classifyWithNeuralNet() {
        @Suppress("UNUSED_VARIABLE")
        val unused = allLaunches
            .flatMap { launches ->
                packagesManager
                    .getInstalledPackages()
                    .map {
                        launches to it
                    }
            }
            .flatMap { (launches, packages) ->
                performClassification(launches, packages)
            }
            .subscribeOn(newThreadScheduler)
            .observeOn(ioScheduler)
            .subscribe({ classifiedPackages ->
                logger.d("replacing classifier identifiers in db")
                classifiedIdentifierDao.deleteAll()
                classifiedIdentifierDao.insert(classifiedPackages.map {
                    ClassifiedIdentifier(
                        identifier = it.pkg.identifier(),
                        score = it.score
                    )
                })
            }, {
                logger.e("error while classifyNeuralNet()", it)
            })
    }

    internal fun makeEncoder(
        launches: List<PackageLaunch>,
        packages: List<Package>
    ) = launches
        .asSequence()
        .map { it.identifier() }
        .plus(packages.map { it.identifier() })
        .toList()
        .let(nnFactory::makeIdentifierEncoder)

    internal fun shouldRetrainClassifier() =
        timeProvider.nowUtcMs() - persistentClassificationInfo.lastTrainingTimeMs > CLASSIFIER_RETRAIN_INTERVAL_MS

    internal fun getClassifier(
        launches: List<PackageLaunch>,
        encoder: IdentifierEncoder
    ): Network {
        val shouldRetrainClassifier = shouldRetrainClassifier()
        logger.d("getClassifier() should retrain $shouldRetrainClassifier")
        if (!shouldRetrainClassifier) {
            val storedClassifier = classifierPersistence.loadClassifier()
            if (storedClassifier != null)
                return storedClassifier
        }

        val (trainingSet, validationSet) = createDataSet(launches, encoder)
        logger.d("created training (${trainingSet.size}) and validation (${validationSet.size}) dataset")

        return trainClassifier(
            trainingSet = trainingSet,
            validationSet = validationSet,
            identifierEncoder = encoder
        )
    }

    internal fun trainClassifier(
        trainingSet: DataSet,
        validationSet: DataSet,
        identifierEncoder: IdentifierEncoder
    ): Network {

        val inputSize = trainingSet.inputDimension.second
        val outputSize = identifierEncoder.encodedSize
        val network = nnFactory.makeClassifier(inputSize = inputSize, outputSize = outputSize)

        val training = nnFactory.makeTraining(
            network = network,
            trainingSet = trainingSet,
            validationSet = validationSet
        )
        training.perform()
        val validationLoss = training.loss.compute(network, validationSet)
        logger.d("Finished training with validation LOSS $validationLoss")
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
        val rawDataSet = launches.map { it.timestampUtc to encoder.encode(it.identifier()) }

        val validationSetElements = mutableListOf<Pair<Long, DoubleArray>>()
            .apply {
                rawDataSet.forEach {
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

        val trainingSetElements = mutableListOf<Pair<Long, DoubleArray>>()
            .apply {
                rawDataSet.forEach {
                    add(it)
                    (0 until 5).forEach { _ ->
                        add(it.first + randomTimeDisplace to it.second)
                    }
                }
            }
            .map { (timestamp, encodedIdentifier) ->
                createEncodedInput(timestamp) to encodedIdentifier
            }

        val trainingSet = DataSet1D
            .Builder(trainingSetElements.size)
            .add(trainingSetElements::get)
            .build()

        return trainingSet to validationSet
    }

    private companion object {
        const val CLASSIFIER_RETRAIN_INTERVAL_MS = 1000L * 60 * 60 * 12
    }
}

