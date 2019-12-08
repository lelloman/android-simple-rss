package com.lelloman.simplerss.ui.launcher.view

import android.os.Bundle
import com.lelloman.common.utils.StubViewDataBinding
import com.lelloman.common.view.BaseActivity
import com.lelloman.simplerss.ui.launcher.viewmodel.LauncherViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class LauncherActivity : BaseActivity<LauncherViewModel, StubViewDataBinding>() {

    override val layoutResId = NO_LAYOUT_RES_ID

    override val viewModel by viewModel<LauncherViewModel>()

    override fun setViewModel(binding: StubViewDataBinding, viewModel: LauncherViewModel) = Unit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.onViewLoaded()
    }
}