package com.lelloman.simplerss.ui.sources.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.lelloman.simplerss.R
import com.lelloman.simplerss.databinding.ActivitySourceBinding
import com.lelloman.simplerss.ui.SimpleRssActivity
import com.lelloman.simplerss.ui.sources.viewmodel.SourceViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class SourceActivity : SimpleRssActivity<SourceViewModel, ActivitySourceBinding>() {

    override val layoutResId = R.layout.activity_source

    override val viewModel by viewModel<SourceViewModel>()

    override fun setViewModel(binding: ActivitySourceBinding, viewModel: SourceViewModel) {
        binding.viewModel = viewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sourceId = intent?.getLongExtra(ARG_SOURCE_ID, 0L)
        if (sourceId == null || sourceId == 0L) {
            finish()
        } else {
            viewModel.onSourceIdLoaded(sourceId)
        }
    }

    companion object {

        private const val ARG_SOURCE_ID = "SourceId"

        fun start(activity: Activity, sourceId: Long?) {
            val intent = Intent(activity, SourceActivity::class.java)
                .putExtra(ARG_SOURCE_ID, sourceId!!)
            activity.startActivity(intent)
        }
    }
}