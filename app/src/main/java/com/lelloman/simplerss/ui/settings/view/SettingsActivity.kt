package com.lelloman.simplerss.ui.settings.view

import android.app.Activity
import android.content.Intent
import com.lelloman.simplerss.R
import com.lelloman.simplerss.databinding.ActivitySettingsBinding
import com.lelloman.simplerss.ui.SimpleRssActivity
import com.lelloman.simplerss.ui.settings.viewmodel.SettingsViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class SettingsActivity :
    SimpleRssActivity<SettingsViewModel, ActivitySettingsBinding>(),
    ClearDataConfirmationDialogFragment.Listener {

    override val layoutResId = R.layout.activity_settings

    override val hasActionBarBackButton = true

    override val viewModel by viewModel<SettingsViewModel>()

    override fun setViewModel(binding: ActivitySettingsBinding, viewModel: SettingsViewModel) {
        binding.viewModel = viewModel
    }

    override fun onClearDataConfirmed() {
        viewModel.onClearDataConfirmed()
    }

    companion object {
        fun start(activity: Activity) {
            activity.startActivity(Intent(activity, SettingsActivity::class.java))
        }
    }
}