package com.lelloman.launcher.ui

import android.graphics.drawable.Drawable
import com.lelloman.common.viewmodel.BaseListItemViewModel
import com.lelloman.launcher.packages.Package

class PackageListItemViewModel : BaseListItemViewModel<Package> {

    var label: CharSequence = ""
        private set

    var icon: Drawable? = null
        private set

    override fun bind(item: Package) {
        label = item.label
        icon = item.drawable
    }
}