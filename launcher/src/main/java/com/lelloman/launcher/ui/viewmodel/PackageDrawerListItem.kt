package com.lelloman.launcher.ui.viewmodel

import com.lelloman.common.utils.model.ModelWithId
import com.lelloman.launcher.packages.Package
import com.lelloman.launcher.ui.AppsDrawerListItem

class PackageDrawerListItem(
    val pkg: Package
) : AppsDrawerListItem, ModelWithId by pkg