package com.lelloman.simplerss.ui_feed.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.jakewharton.rxbinding4.appcompat.itemClicks
import com.jakewharton.rxbinding4.swiperefreshlayout.refreshes
import com.lelloman.simplerss.ui_base.BaseFragment
import com.lelloman.simplerss.ui_feed.R
import com.lelloman.simplerss.ui_feed.databinding.FragmentFeedBinding
import com.lelloman.simplerss.ui_feed.viewmodel.FeedViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.core.Observable

@AndroidEntryPoint
class FeedFragment : BaseFragment<FeedViewModel.State, FeedViewModel.Event, FeedViewModel.Action>() {

    override val viewModel: FeedViewModel by viewModels()

    private lateinit var binding: FragmentFeedBinding

    override val actionBar: Toolbar get() = binding.toolbar

    private val adapter = FeedAdapter { viewModel.processAction(FeedViewModel.Action.FeedItemClicked(it)) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentFeedBinding.inflate(layoutInflater).let { binding ->
            this.binding = binding
            binding.recyclerView.layoutManager = LinearLayoutManager(requireActivity())
            binding.recyclerView.adapter = adapter
            setHasOptionsMenu(true)
            binding.appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
                binding.swipeRefreshLayout.isEnabled = binding.swipeRefreshLayout.isRefreshing || verticalOffset == 0
            })
            binding.root
        }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.feed, menu)
    }

    override fun collectViewActions(): Observable<FeedViewModel.Action> = with(binding) {
        Observable.merge(
            swipeRefreshLayout.refreshes().map { FeedViewModel.Action.FeedPulledToRefresh },
            menuItemsClicks()
        )
    }

    override fun handleEvent(event: FeedViewModel.Event) {

    }

    override fun render(state: FeedViewModel.State) {
        with(binding) {
            progressBar.visibility = if (state.isLoadingFeed) View.VISIBLE else View.GONE
            swipeRefreshLayout.visibility = if (state.isLoadingFeed) View.GONE else View.VISIBLE
            swipeRefreshLayout.isRefreshing = state.isRefreshing
            adapter.onNewList(state.feedItems)
        }
    }

    private fun menuItemsClicks(): Observable<FeedViewModel.Action> = binding.toolbar
        .itemClicks()
        .map {
            when (val itemId = it.itemId) {
                R.id.action_about -> FeedViewModel.Action.AboutButtonClicked
                R.id.action_settings -> FeedViewModel.Action.SettingsButtonClicked
                R.id.action_sources -> FeedViewModel.Action.SourcesButtonClicked
                R.id.action_discover -> FeedViewModel.Action.DiscoverButtonClicked
                R.id.action_refresh -> FeedViewModel.Action.RefreshButtonClicked
                else -> error("Unknown item id $itemId")
            }
        }
}