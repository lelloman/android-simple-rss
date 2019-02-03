package com.lelloman.simplerss.ui.launcher.view

import com.lelloman.common.utils.StubViewDataBinding
import com.lelloman.common.view.BaseActivity
import com.lelloman.simplerss.ui.launcher.viewmodel.LauncherViewModel

class LauncherActivity : BaseActivity<LauncherViewModel, StubViewDataBinding>() {

    override val layoutResId = NO_LAYOUT_RES_ID

    override fun getViewModelClass() = LauncherViewModel::class.java

    override fun setViewModel(binding: StubViewDataBinding, viewModel: LauncherViewModel) = Unit
}