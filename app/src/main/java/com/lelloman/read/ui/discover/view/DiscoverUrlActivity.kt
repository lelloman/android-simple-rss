package com.lelloman.read.ui.discover.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.lelloman.common.navigation.DeepLink
import com.lelloman.common.navigation.DeepLinkStartable
import com.lelloman.common.view.BaseActivity
import com.lelloman.read.R
import com.lelloman.read.databinding.ActivityDiscoverUrlBinding
import com.lelloman.read.ui.discover.viewmodel.DiscoverUrlViewModel

class DiscoverUrlActivity : BaseActivity<DiscoverUrlViewModel, ActivityDiscoverUrlBinding>() {

    override val layoutResId = R.layout.activity_discover_url

    override fun getViewModelClass() = DiscoverUrlViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasActionBarBackButton()
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