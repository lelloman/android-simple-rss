package com.lelloman.launcher.packages

import android.graphics.drawable.Drawable
import com.lelloman.common.utils.model.ModelWithId

data class Package(
    override val id: Long,
    val label: CharSequence,
    val packageName: String,
    val activityName: String,
    val drawable: Drawable
) : ModelWithId {

    fun identifier() = "$packageName/$activityName"
}