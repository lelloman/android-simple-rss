package com.lelloman.read.ui.walkthrough

import com.lelloman.common.utils.ModelWithId
import com.lelloman.read.core.view.AppTheme

class ThemeListItem(
    override val id: Long,
    val theme: AppTheme,
    var isEnabled: Boolean
) : ModelWithId