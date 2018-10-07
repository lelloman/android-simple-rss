package com.lelloman.launcher.classification.model

import com.lelloman.launcher.packages.Package

data class ClassifiedPackage(
    val pkg: Package,
    val score: Double
)