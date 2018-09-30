package com.lelloman.launcher.ui.viewmodel

import android.graphics.drawable.Drawable
import com.lelloman.common.viewmodel.BaseListItemViewModel

class PackageListItemViewModel : BaseListItemViewModel<PackageDrawerListItem> {

    var label: CharSequence = ""
        private set

    var icon: Drawable? = null
        private set

    override fun bind(item: PackageDrawerListItem) = item.pkg.let { pkg ->
        label = pkg.label
        icon = pkg.drawable
    }
}