package com.lelloman.read.ui.discover.view

import android.app.Activity
import android.content.Intent
import com.lelloman.read.R
import com.lelloman.read.core.view.BaseActivity
import com.lelloman.read.databinding.ActivityDiscoverUrlBinding
import com.lelloman.read.ui.discover.viewmodel.DiscoverUrlViewModel

class DiscoverUrlActivity : BaseActivity<DiscoverUrlViewModel, ActivityDiscoverUrlBinding>() {

    override fun getLayoutId() = R.layout.activity_discover_url

    override fun getViewModelClass() = DiscoverUrlViewModel::class.java

    companion object {
        fun start(activity: Activity) {
            activity.startActivity(Intent(activity, DiscoverUrlActivity::class.java))
        }
    }
}