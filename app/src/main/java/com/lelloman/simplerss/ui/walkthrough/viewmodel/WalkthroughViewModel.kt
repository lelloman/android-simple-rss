package com.lelloman.simplerss.ui.walkthrough.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.view.View
import com.lelloman.common.view.AppTheme
import com.lelloman.common.viewmodel.BaseViewModel

abstract class WalkthroughViewModel(dependencies: Dependencies)
    : com.lelloman.simplerss.ui.common.viewmodel.IDiscoverUrlViewModel, BaseViewModel(dependencies) {

    abstract val themes: MutableLiveData<List<com.lelloman.simplerss.ui.walkthrough.ThemeListItem>>

    abstract fun onCloseClicked(view: View)

    abstract fun onFirstPageOkClicked(view: View)

    abstract fun onMeteredConnectionYesClicked(view: View)
    abstract fun onMeteredConnectionNoClicked(view: View)

    abstract fun onThemeClicked(theme: AppTheme)

}