package com.lelloman.read.ui.sources.view

import android.app.Activity
import android.content.Intent
import com.lelloman.read.R
import com.lelloman.read.core.view.BaseActivity
import com.lelloman.read.databinding.ActivitySourceBinding
import com.lelloman.read.ui.sources.viewmodel.SourceViewModel

class SourceActivity : BaseActivity<SourceViewModel, ActivitySourceBinding>() {

    override fun getLayoutId() = R.layout.activity_source

    override fun getViewModelClass() = SourceViewModel::class.java

    companion object {

        private const val ARG_SOURCE_ID = "SourceId"

        fun start(activity: Activity, sourceId: Long) {
            val intent = Intent(activity, SourceActivity::class.java)
                .putExtra(ARG_SOURCE_ID, sourceId)
            activity.startActivity(intent)
        }
    }
}