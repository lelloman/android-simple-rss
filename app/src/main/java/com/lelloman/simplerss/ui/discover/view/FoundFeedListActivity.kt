package com.lelloman.simplerss.ui.discover.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.lelloman.common.navigation.DeepLink
import com.lelloman.common.navigation.DeepLinkStartable
import com.lelloman.common.view.BaseActivity
import com.lelloman.simplerss.R
import com.lelloman.simplerss.databinding.ActivityFoundFeedListBinding
import com.lelloman.simplerss.feed.finder.FoundFeed
import com.lelloman.simplerss.navigation.SimpleRssNavigationScreen.Companion.ARG_URL
import com.lelloman.simplerss.ui.discover.viewmodel.FoundFeedListViewModel
import dagger.android.AndroidInjection

class FoundFeedListActivity
    : BaseActivity<FoundFeedListViewModel, ActivityFoundFeedListBinding>(),
    AddFoundFeedsConfirmationDialogFragment.Listener {

    private lateinit var adapter: FoundFeedsAdapter

    private var addAllAction: MenuItem? = null

    override val layoutResId = R.layout.activity_found_feed_list

    override fun getViewModelClass() = FoundFeedListViewModel::class.java

    override fun setViewModel(binding: ActivityFoundFeedListBinding, viewModel: FoundFeedListViewModel) {
        binding.viewModel = viewModel
    }

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

        var deepLinkStartable = object : DeepLinkStartable {
            override fun start(context: Context, deepLink: DeepLink) {
                val intent = Intent(context, FoundFeedListActivity::class.java)
                    .putExtra(ARG_URL, deepLink.getString(ARG_URL))
                if (context !is Activity) {
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(intent)
            }
        }
            internal set
    }
}