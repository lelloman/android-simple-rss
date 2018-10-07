package com.lelloman.launcher.classification

import com.lelloman.common.logger.LoggerFactory
import com.lelloman.common.utils.TimeProvider
import com.lelloman.launcher.classification.model.ClassifiedPackage
import com.lelloman.launcher.classification.model.PackageLaunchesDataset
import com.lelloman.launcher.packages.Package
import com.lelloman.launcher.persistence.PackageLaunchDao
import com.lelloman.launcher.persistence.model.PackageLaunch
import io.reactivex.Single

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

    override fun classify(packages: List<Package>) = classifyByMostUsed(packages)

    private fun classifyWithNeuralNet(packages: List<Package>): Single<List<ClassifiedPackage>> = allLaunches
        .map { launches ->
            val launchesEncoder = neuralNetFactory.createLaunchesEncoder(launches)
            val input = createLaunchesInput(launches)
            val output = createLaunchesOutput(launches, launchesEncoder)
            val dataset = PackageLaunchesDataset(input = input, output = output)
            val classifier = neuralNetFactory.createPackageLaunchClassifier(launches, launchesEncoder)
            classifier.train(dataset)
            packages.map {
                ClassifiedPackage(
                    pkg = it,
                    score = classifier.classify(it)
                )
            }
        }

    private fun createLaunchesInput(launches: List<PackageLaunch>): Array<DoubleArray> = launches
        .map {
            val time = it.timestampUtc
            val weekTime = timeProvider.getParsedWeekTime(time)
            val dayTime = timeProvider.getParsedDayTime(time)

            val encodedWeekTime = timeEncoder.encodeWeekTime(weekTime)
            val encodedDayTime = timeEncoder.encodeDayTime(dayTime)

            encodedWeekTime.plus(encodedDayTime)
        }
        .toTypedArray()

    private fun createLaunchesOutput(launches: List<PackageLaunch>, encoder: PackageLaunchEncoder): Array<DoubleArray> {
        return Array(launches.size) {
            encoder.encode(launches[it])
        }
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

