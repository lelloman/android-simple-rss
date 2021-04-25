package com.lelloman.simplerss.ui_source

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.lelloman.simplerss.ui_base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.core.Observable

@AndroidEntryPoint
class SourcesFragment : BaseFragment<SourcesViewModel.State, SourcesViewModel.Event, SourcesViewModel.Action>() {

    override val viewModel: SourcesViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sources, container, false)
    }

    override fun collectViewActions(): Observable<SourcesViewModel.Action> {
        return Observable.empty()
    }

    override fun handleEvent(event: SourcesViewModel.Event) {

    }

    override fun render(state: SourcesViewModel.State) {

    }
}