package com.lelloman.simplerss.ui.sources.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.lelloman.common.view.BaseActivity
import com.lelloman.simplerss.R
import com.lelloman.simplerss.databinding.ActivitySourceBinding

class SourceActivity : BaseActivity<com.lelloman.simplerss.ui.sources.viewmodel.SourceViewModel, ActivitySourceBinding>() {

    override val layoutResId = R.layout.activity_source

    override fun getViewModelClass() = com.lelloman.simplerss.ui.sources.viewmodel.SourceViewModel::class.java

    override fun setViewModel(binding: ActivitySourceBinding, viewModel: com.lelloman.simplerss.ui.sources.viewmodel.SourceViewModel) {
        binding.viewModel = viewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sourceId = intent?.getLongExtra(com.lelloman.simplerss.ui.sources.view.SourceActivity.Companion.ARG_SOURCE_ID, 0L)
        if (sourceId == null || sourceId == 0L) {
            finish()
        } else {
            viewModel.onSourceIdLoaded(sourceId)
        }
    }

    companion object {

        private const val ARG_SOURCE_ID = "SourceId"

        fun start(activity: Activity, sourceId: Long?) {
            val intent = Intent(activity, com.lelloman.simplerss.ui.sources.view.SourceActivity::class.java)
                .putExtra(com.lelloman.simplerss.ui.sources.view.SourceActivity.Companion.ARG_SOURCE_ID, sourceId!!)
            activity.startActivity(intent)
        }
    }
}