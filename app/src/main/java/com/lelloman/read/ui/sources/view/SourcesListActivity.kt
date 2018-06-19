package com.lelloman.read.ui.sources.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.lelloman.read.R
import com.lelloman.read.core.ResourceProvider
import com.lelloman.read.core.TimeDiffCalculator
import com.lelloman.read.core.view.BaseActivity
import com.lelloman.read.databinding.ActivitySourcesListBinding
import com.lelloman.read.ui.sources.viewmodel.SourcesListViewModel
import dagger.android.AndroidInjection
import javax.inject.Inject

class SourcesListActivity
    : BaseActivity<SourcesListViewModel, ActivitySourcesListBinding>() {

    private lateinit var adapter: SourcesAdapter

    @Inject
    lateinit var timeDiffCalculator: TimeDiffCalculator

    @Inject
    lateinit var resourceProvider: ResourceProvider

    override fun getLayoutId() = R.layout.activity_sources_list

    override fun getViewModelClass() = SourcesListViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)

        adapter = SourcesAdapter(
            timeDiffCalculator = timeDiffCalculator,
            resourceProvider = resourceProvider,
            sourceClickedListener = viewModel::onSourceClicked
        )

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        binding.viewModel = viewModel
        viewModel.sources.observe(this, adapter)
    }

    companion object {
        fun start(activity: Activity) {
            activity.startActivity(Intent(activity, SourcesListActivity::class.java))
        }
    }

}
