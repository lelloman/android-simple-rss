package com.lelloman.simplerss.ui.walkthrough.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.lelloman.common.view.AppTheme
import com.lelloman.common.viewmodel.BaseViewModel
import com.lelloman.simplerss.ui.common.viewmodel.IDiscoverUrlViewModel
import com.lelloman.simplerss.ui.walkthrough.ThemeListItem

abstract class WalkthroughViewModel(dependencies: Dependencies)
    : IDiscoverUrlViewModel, BaseViewModel(dependencies) {

    abstract val themes: MutableLiveData<List<ThemeListItem>>

    abstract val firstPageText: LiveData<CharSequence>
    abstract val nextButtonVisible: LiveData<Boolean>

    abstract override fun onCloseClicked()

    abstract fun onNextButtonClicked()

    abstract fun onMeteredConnectionYesClicked()
    abstract fun onMeteredConnectionNoClicked()

    abstract fun onThemeClicked(theme: AppTheme)

    abstract fun onPageSelected(pageIndex: Int)

}