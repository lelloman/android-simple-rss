package com.lelloman.read.ui.settings.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.lelloman.read.R
import com.lelloman.read.core.view.BaseActivity
import com.lelloman.read.databinding.ActivitySettingsBinding
import com.lelloman.read.ui.settings.viewmodel.SettingsViewModel

class SettingsActivity : BaseActivity<SettingsViewModel, ActivitySettingsBinding>() {

    override fun getLayoutId() = R.layout.activity_settings

    override fun getViewModelClass() = SettingsViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        binding.viewModel = viewModel
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {
        fun start(activity: Activity) {
            activity.startActivity(Intent(activity, SettingsActivity::class.java))
        }
    }
}