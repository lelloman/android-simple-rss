package com.lelloman.simplerss.ui_source.remote

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.lelloman.simplerss.ui_base.BaseFragment
import com.lelloman.simplerss.ui_source.databinding.FragmentRemoteSourceBinding
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.core.Observable

@AndroidEntryPoint
class RemoteSourceFragment :
    BaseFragment<RemoteSourceViewModel.State, RemoteSourceViewModel.Event, RemoteSourceViewModel.Action>() {

    override val viewModel: RemoteSourceViewModel by viewModels()

    private lateinit var binding: FragmentRemoteSourceBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentRemoteSourceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun collectViewActions(): Observable<RemoteSourceViewModel.Action> {
        return Observable.empty()
    }

    override fun handleEvent(event: RemoteSourceViewModel.Event) {

    }

    override fun render(state: RemoteSourceViewModel.State) {

    }
}