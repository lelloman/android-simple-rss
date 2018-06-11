package com.lelloman.read.sources.view

import android.app.Activity
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.lelloman.read.R
import com.lelloman.read.core.ViewModelFactory
import com.lelloman.read.databinding.ActivitySourcesListBinding
import com.lelloman.read.sources.viewmodel.SourcesListViewModel
import dagger.android.AndroidInjection
import javax.inject.Inject

class SourcesListActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySourcesListBinding

    @Inject
    lateinit var adapter: SourcesAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelFactory<SourcesListViewModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        setContentView(R.layout.activity_sources_list)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sources_list)
        binding.setLifecycleOwner(this)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        ViewModelProviders.of(this, viewModelFactory).get(SourcesListViewModel::class.java).let { viewModel ->
            binding.viewModel = viewModel
            viewModel.sources.observe(this, adapter)
        }
    }

    companion object {
        fun start(activity: Activity) {
            activity.startActivity(Intent(activity, SourcesListActivity::class.java))
        }
    }

}
