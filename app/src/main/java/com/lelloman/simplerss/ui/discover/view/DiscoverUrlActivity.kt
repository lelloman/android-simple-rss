package com.lelloman.simplerss.ui.discover.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.lelloman.common.navigation.DeepLink
import com.lelloman.common.navigation.DeepLinkStartable
import com.lelloman.common.view.BaseActivity
import com.lelloman.simplerss.R
import com.lelloman.simplerss.databinding.ActivityDiscoverUrlBinding
import com.lelloman.simplerss.ui.discover.viewmodel.DiscoverUrlViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class DiscoverUrlActivity : BaseActivity<DiscoverUrlViewModel, ActivityDiscoverUrlBinding>() {

    override val layoutResId = R.layout.activity_discover_url

    override val viewModel by viewModel<DiscoverUrlViewModel>()

    override val hasActionBarBackButton = true

    override fun setViewModel(binding: ActivityDiscoverUrlBinding, viewModel: DiscoverUrlViewModel) {
        binding.viewModel = viewModel
    }

    companion object {

        var deepLinkStartable = object : DeepLinkStartable {
            override fun start(context: Context, deepLink: DeepLink) {
                val intent = Intent(context, DiscoverUrlActivity::class.java)
                if (context !is Activity) {
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(intent)
            }
        }
    }
}