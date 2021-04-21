package com.lelloman.simplerss.ui_feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.lelloman.simplerss.ui_base.BaseFragment
import com.lelloman.simplerss.ui_feed.databinding.FragmentFeedBinding
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.core.Observable

@AndroidEntryPoint
class FeedFragment : BaseFragment<FeedViewModel.State, FeedViewModel.Event, FeedViewModel.Action>() {

    override val viewModel: FeedViewModel by viewModels()

    private lateinit var binding: FragmentFeedBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentFeedBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun collectViewActions(): Observable<FeedViewModel.Action> = with(binding) {
        Observable.merge(
            aboutButton.mapClicks { FeedViewModel.Action.AboutButtonClicked },
            settingsButton.mapClicks { FeedViewModel.Action.SettingsButtonClicked }
        )
    }

    override fun handleEvent(event: FeedViewModel.Event) {

    }

    override fun render(state: FeedViewModel.State) {
        with(binding) {
            progressBar.visibility = if (state.isLoading) View.VISIBLE else View.GONE
            aboutButton.isEnabled = !state.isLoading
            settingsButton.isEnabled = !state.isLoading
        }
    }
}