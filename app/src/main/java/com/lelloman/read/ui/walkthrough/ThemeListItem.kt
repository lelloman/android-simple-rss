package com.lelloman.read.ui.walkthrough

import com.lelloman.common.utils.model.ModelWithId
import com.lelloman.common.view.AppTheme

class ThemeListItem(
    override val id: Long,
    val theme: AppTheme,
    var isEnabled: Boolean
) : ModelWithId