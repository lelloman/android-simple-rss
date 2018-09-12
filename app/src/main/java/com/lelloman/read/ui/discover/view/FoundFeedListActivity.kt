package com.lelloman.read.ui.discover.view

import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.lelloman.read.R
import com.lelloman.read.core.view.BaseActivity
import com.lelloman.read.core.view.ResourceProvider
import com.lelloman.read.databinding.ActivityFoundFeedListBinding
import com.lelloman.read.feed.finder.FoundFeed
import com.lelloman.read.ui.discover.viewmodel.FoundFeedListViewModel
import dagger.android.AndroidInjection
import javax.inject.Inject

class FoundFeedListActivity
    : BaseActivity<FoundFeedListViewModel, ActivityFoundFeedListBinding>(),
    AddFoundFeedsConfirmationDialogFragment.Listener {

    @Inject
    lateinit var resourceProvider: ResourceProvider

    private lateinit var adapter: FoundFeedsAdapter

    private var addAllAction: MenuItem? = null

    override val layoutResId = R.layout.activity_found_feed_list

    override fun getViewModelClass() = FoundFeedListViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)

        supportActionBar?.apply {
            setHomeButtonEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            title = intent?.getStringExtra(ARG_URL)
            setHomeAsUpIndicator(R.drawable.ic_close_white_24dp)
        }

        adapter = FoundFeedsAdapter(
            resourceProvider = resourceProvider,
            onFoundFeedClickListener = viewModel::onFoundFeedClicked
        )
        binding.discoverRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.discoverRecyclerView.adapter = adapter
        viewModel.foundFeeds.observe(this, adapter)

        binding.viewModel = viewModel

        viewModel.isFindingFeeds.observe(this, Observer {
            addAllAction?.isEnabled = it != true
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_found_feeds_list, menu)
        addAllAction = menu.findItem(R.id.action_add_all)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when(item.itemId){
        android.R.id.home -> {
            viewModel.onCloseClicked()
            true
        }
        R.id.action_add_all -> {
            viewModel.onAddAllClicked()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onAddAllFoundFeedsConfirmationClicked(foundFeeds: List<FoundFeed>) {
        viewModel.onAddAllFoundFeedsConfirmationClicked(foundFeeds)
    }

    companion object {

        private const val ARG_URL = "Url"

        fun start(activity: Activity, url: String) {
            activity.startActivity(
                Intent(activity, FoundFeedListActivity::class.java)
                    .putExtra(ARG_URL, url)
            )
        }
    }
}