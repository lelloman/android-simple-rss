package com.lelloman.simplerss.ui.debug.view

import android.app.Activity
import android.content.Intent
import com.lelloman.simplerss.R
import com.lelloman.simplerss.databinding.ActivityDebugBinding
import com.lelloman.simplerss.ui.SimpleRssActivity
import com.lelloman.simplerss.ui.debug.viewmodel.DebugViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class DebugActivity :
    SimpleRssActivity<DebugViewModel, ActivityDebugBinding>(),
    ResetDbConfirmationDialogFragment.Listener,
    ResetSharedPrefsConfirmationDialogFragment.Listener {

    override val layoutResId = R.layout.activity_debug

    override val viewModel by viewModel<DebugViewModel>()

    override fun setViewModel(binding: ActivityDebugBinding, viewModel: DebugViewModel) {
        binding.viewModel = viewModel
    }

    override fun onResetDbConfirmed() = viewModel.onResetDbConfirmed()

    override fun onResetSharedPrefsConfirmed() = viewModel.onResetSharedPrefsConfirmed()

    companion object {
        fun start(activity: Activity) {
            activity.startActivity(Intent(activity, DebugActivity::class.java))
        }
    }
}