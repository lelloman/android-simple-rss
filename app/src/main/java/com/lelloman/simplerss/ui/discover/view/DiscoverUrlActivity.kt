package com.lelloman.simplerss.ui.discover.view

import android.app.Activity
import android.content.Intent
import com.lelloman.simplerss.R
import com.lelloman.simplerss.databinding.ActivityDiscoverUrlBinding
import com.lelloman.simplerss.ui.SimpleRssActivity
import com.lelloman.simplerss.ui.discover.viewmodel.DiscoverUrlViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class DiscoverUrlActivity : SimpleRssActivity<DiscoverUrlViewModel, ActivityDiscoverUrlBinding>() {

    override val layoutResId = R.layout.activity_discover_url

    override val viewModel by viewModel<DiscoverUrlViewModel>()

    override val hasActionBarBackButton = true

    override fun setViewModel(
        binding: ActivityDiscoverUrlBinding,
        viewModel: DiscoverUrlViewModel
    ) {
        binding.viewModel = viewModel
    }

    companion object {
        fun start(activity: Activity) {
            activity.startActivity(Intent(activity, DiscoverUrlActivity::class.java))
        }
    }
}