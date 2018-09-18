package com.lelloman.common.view.actionevent

import com.lelloman.common.view.AppTheme

data class ThemeChangedActionEvent(
    val newTheme: AppTheme
) : ViewActionEvent