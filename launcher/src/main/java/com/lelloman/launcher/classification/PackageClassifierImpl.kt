package com.lelloman.launcher.classification

import com.lelloman.launcher.packages.Package
import io.reactivex.Single
import java.util.*

class PackageClassifierImpl : PackageClassifier {

    private val random = Random()

    override fun classify(packages: List<Package>): Single<List<ClassifiedPackage>> {
        return Single
            .fromCallable {
                packages.map {
                    ClassifiedPackage(it, random.nextDouble())
                }
            }
    }
}