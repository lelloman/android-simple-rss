package com.lelloman.simplerss.ui_source.local

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.lelloman.simplerss.ui_base.BaseFragment
import com.lelloman.simplerss.ui_source.databinding.FragmentLocalSourceBinding
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.core.Observable

@AndroidEntryPoint
class LocalSourceFragment :
    BaseFragment<LocalSourceViewModel.State, LocalSourceViewModel.Event, LocalSourceViewModel.Action>() {

    override val viewModel: LocalSourceViewModel by viewModels()

    private lateinit var binding: FragmentLocalSourceBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentLocalSourceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun collectViewActions(): Observable<LocalSourceViewModel.Action> {
        return Observable.empty()
    }

    override fun handleEvent(event: LocalSourceViewModel.Event) {

    }

    override fun render(state: LocalSourceViewModel.State) {

    }
}