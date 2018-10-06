package com.lelloman.launcher.testutils

import android.graphics.drawable.Drawable
import android.support.test.InstrumentationRegistry.getInstrumentation
import com.lelloman.launcher.R
import com.lelloman.launcher.packages.Package

private val DRAWABLE: Drawable by lazy {
    getInstrumentation().targetContext.getDrawable(R.mipmap.ic_launcher)
}

fun pkg(index: Int = 0) = Package(
    id = index.toLong(),
    label = "label $index",
    packageName = "packageName $index",
    activityName = "activityName $index",
    drawable = DRAWABLE
)