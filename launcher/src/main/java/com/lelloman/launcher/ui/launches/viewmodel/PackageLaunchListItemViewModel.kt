package com.lelloman.launcher.ui.launches.viewmodel

import android.graphics.drawable.Drawable
import com.lelloman.common.view.SemanticTimeProvider
import com.lelloman.common.viewmodel.BaseListItemViewModel
import com.lelloman.launcher.ui.launches.PackageLaunchListItem

class PackageLaunchListItemViewModel(
    private val semanticTimeProvider: SemanticTimeProvider
) : BaseListItemViewModel<Long, PackageLaunchListItem> {

    var icon: Drawable? = null
        private set

    var packageName = ""
        private set

    var dateTime = ""
        private set

    override fun bind(item: PackageLaunchListItem) {
        icon = item.icon
        packageName = item.packageLaunch.packageName + "/" + item.packageLaunch.activityName
        val dateTimeString = semanticTimeProvider.getDateTimeString(item.packageLaunch.timestampUtc)
        val timeDiffString = semanticTimeProvider.getTimeDiffString(item.packageLaunch.timestampUtc)
        dateTime = "$dateTimeString - $timeDiffString"
    }
}