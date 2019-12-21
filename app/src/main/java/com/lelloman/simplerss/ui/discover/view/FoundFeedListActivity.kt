package com.lelloman.simplerss.ui.discover.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.lelloman.simplerss.R
import com.lelloman.simplerss.databinding.ActivityFoundFeedListBinding
import com.lelloman.simplerss.feed.finder.FoundFeed
import com.lelloman.simplerss.ui.SimpleRssActivity
import com.lelloman.simplerss.ui.discover.viewmodel.FoundFeedListViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class FoundFeedListActivity
    : SimpleRssActivity<FoundFeedListViewModel, ActivityFoundFeedListBinding>(),
    AddFoundFeedsConfirmationDialogFragment.Listener {

    private lateinit var adapter: FoundFeedsAdapter

    private var addAllAction: MenuItem? = null

    override val layoutResId = R.layout.activity_found_feed_list

    override val viewModel by viewModel<FoundFeedListViewModel>()

    override fun setViewModel(
        binding: ActivityFoundFeedListBinding,
        viewModel: FoundFeedListViewModel
    ) {
        binding.viewModel = viewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.apply {
            setHomeButtonEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            title = intent?.getStringExtra(ARG_URL)
            setHomeAsUpIndicator(R.drawable.ic_close_white_24dp)
        }

        adapter = FoundFeedsAdapter(
            onFoundFeedClickListener = viewModel::onFoundFeedClicked
        )
        binding.discoverRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.discoverRecyclerView.adapter = adapter
        viewModel.foundFeeds.observe(this, adapter)

        viewModel.isFindingFeeds.observe(this, Observer {
            addAllAction?.isEnabled = it != true
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_found_feeds_list, menu)
        addAllAction = menu.findItem(R.id.action_add_all)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
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
            val intent = Intent(activity, FoundFeedListActivity::class.java)
                .putExtra(ARG_URL, url)
            activity.startActivity(intent)
        }
    }
}