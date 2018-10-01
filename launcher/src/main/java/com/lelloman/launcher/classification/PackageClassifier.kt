package com.lelloman.launcher.classification

import com.lelloman.launcher.packages.Package
import io.reactivex.Single

interface PackageClassifier {

    fun classify(packages: List<Package>): Single<List<ClassifiedPackage>>


}