package com.lelloman.read.ui.sources.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.lelloman.read.R
import com.lelloman.read.core.view.BaseActivity
import com.lelloman.read.databinding.ActivitySourceBinding
import com.lelloman.read.ui.sources.viewmodel.SourceViewModel
import dagger.android.AndroidInjection

class SourceActivity : BaseActivity<SourceViewModel, ActivitySourceBinding>() {

    override fun getLayoutId() = R.layout.activity_source

    override fun getViewModelClass() = SourceViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)

        binding.viewModel = viewModel
        val sourceId = intent?.getLongExtra(ARG_SOURCE_ID, 0L)
        if (sourceId == null || sourceId == 0L) {
            finish()
        } else {
            viewModel.onSourceIdLoaded(sourceId)
        }
    }

    companion object {

        private const val ARG_SOURCE_ID = "SourceId"

        fun start(activity: Activity, sourceId: Long) {
            val intent = Intent(activity, SourceActivity::class.java)
                .putExtra(ARG_SOURCE_ID, sourceId)
            activity.startActivity(intent)
        }
    }
}