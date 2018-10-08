package com.lelloman.launcher.classification

import android.support.annotation.VisibleForTesting
import com.lelloman.common.logger.LoggerFactory
import com.lelloman.common.utils.TimeProvider
import com.lelloman.launcher.packages.Package
import com.lelloman.launcher.persistence.PackageLaunchDao
import com.lelloman.launcher.persistence.model.PackageLaunch
import com.lelloman.nn.dataset.DataSet
import com.lelloman.nn.dataset.DataSet1D
import io.reactivex.Single
import java.util.*

class PackageClassifierImpl(
    private val packageLaunchDao: PackageLaunchDao,
    private val neuralNetFactory: NeuralNetFactory,
    private val timeProvider: TimeProvider,
    private val timeEncoder: TimeEncoder,
    loggerFactory: LoggerFactory
) : PackageClassifier {

    private val logger = loggerFactory.getLogger(PackageClassifierImpl::class.java)

    private val allLaunches: Single<List<PackageLaunch>>
        get() = packageLaunchDao
            .getAll()
            .firstOrError()

    private val random = Random()

    private val randomTimeDisplace get() = ((random.nextDouble() - .5) * 10 * 60 * 1000).toLong()

    override fun classify(packages: List<Package>) = classifyWithNeuralNet(packages)

    @VisibleForTesting
    fun classifyWithNeuralNet(packages: List<Package>): Single<List<ClassifiedPackage>> = allLaunches
        .map { launches ->
            logger.d("classifyWithNeuralNet() provided ${launches.size} launches")
            val identifiers = launches.map { it.identifier() }.plus(packages.map { it.identifier })
            val launchesEncoder = neuralNetFactory.createLaunchesEncoder(identifiers)
            val (trainingSet, validationSet) = createDataSet(launches, launchesEncoder)
            logger.d("created training (${trainingSet.size}) and validation (${validationSet.size}) dataset")
            val classifier = neuralNetFactory.createPackageLaunchClassifier(
                trainingSet = trainingSet,
                validationSet = validationSet,
                timeProvider = timeProvider,
                launchesEncoder = launchesEncoder
            )
            val classification = classifier.classify(createEncodedInput())
            packages.map {
                val encodedPackage = launchesEncoder.encode(it.identifier)
                ClassifiedPackage(
                    pkg = it,
                    score = getScoreForPackage(classification, encodedPackage)
                )
            }
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

    private fun createDataSet(launches: List<PackageLaunch>, encoder: PackageLaunchEncoder): Pair<DataSet, DataSet> {
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

