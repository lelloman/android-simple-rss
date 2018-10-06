package com.lelloman.launcher.classification

import android.util.Log
import com.lelloman.launcher.packages.Package
import com.lelloman.launcher.packages.PackageLaunchDao
import com.lelloman.launcher.persistence.model.PackageLaunch
import io.reactivex.Single

class PackageClassifierImpl(
    private val packageLaunchDao: PackageLaunchDao
) : PackageClassifier {

    override fun classify(packages: List<Package>): Single<List<ClassifiedPackage>> = packageLaunchDao
        .getAll()
        .firstOrError()
        .map { launches ->
            val launchesMap = mutableMapOf<String, Int>()
            launches.forEach { launch ->
                val currentScore = if (launchesMap.containsKey(launch.identifier())) {
                    launchesMap[launch.identifier()]!!
                } else {
                    0
                }
                launchesMap[launch.identifier()] = currentScore + 1
                Log.d("ASD", "${currentScore + 1} ${launch.identifier()}")
            }
            packages
                .map { pkg ->
                    val score = if (launchesMap.containsKey(pkg.identifier())) {
                        launchesMap[pkg.identifier()]!!.toDouble()
                    } else 0.0
                    ClassifiedPackage(pkg, score)
                }
        }

    private fun Package.identifier() = this.packageName + this.activityName
    private fun PackageLaunch.identifier() = this.packageName + this.activityName
}

