package com.lelloman.simplerss.ui.settings.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.lelloman.common.navigation.DeepLink
import com.lelloman.common.navigation.DeepLinkStartable
import com.lelloman.common.view.BaseActivity
import com.lelloman.simplerss.R
import com.lelloman.simplerss.databinding.ActivitySettingsBinding

class SettingsActivity : BaseActivity<com.lelloman.simplerss.ui.settings.viewmodel.SettingsViewModel, ActivitySettingsBinding>() {

    override val layoutResId = R.layout.activity_settings

    override val hasActionBarBackButton = true

    override fun getViewModelClass() = com.lelloman.simplerss.ui.settings.viewmodel.SettingsViewModel::class.java

    override fun setViewModel(binding: ActivitySettingsBinding, viewModel: com.lelloman.simplerss.ui.settings.viewmodel.SettingsViewModel) {
        binding.viewModel = viewModel
    }

    companion object {

        var deepLinkStartable = object : DeepLinkStartable {
            override fun start(context: Context, deepLink: DeepLink) {
                val intent = Intent(context, com.lelloman.simplerss.ui.settings.view.SettingsActivity::class.java)
                if (context !is Activity) {
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(intent)
            }
        }
            internal set
    }
}