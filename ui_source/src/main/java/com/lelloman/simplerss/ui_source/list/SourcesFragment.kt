package com.lelloman.simplerss.ui_source.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.leinardi.android.speeddial.SpeedDialActionItem
import com.leinardi.android.speeddial.SpeedDialView
import com.lelloman.simplerss.ui_base.BaseFragment
import com.lelloman.simplerss.ui_source.R
import com.lelloman.simplerss.ui_source.databinding.FragmentSourcesBinding
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.core.Observable

@AndroidEntryPoint
class SourcesFragment : BaseFragment<SourcesViewModel.State, SourcesViewModel.Event, SourcesViewModel.Action>() {

    override val viewModel: SourcesViewModel by viewModels()

    private lateinit var binding: FragmentSourcesBinding

    override val actionBar: Toolbar get() = binding.toolbar

    private val adapter = SourcesAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSourcesBinding.inflate(inflater, container, false)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        binding.recyclerView.adapter = adapter
        viewModel.addMenuItems.forEach { addMenuItem ->
            val actionItem = SpeedDialActionItem.Builder(addMenuItem.actionId, addMenuItem.iconResId)
                .setLabel(addMenuItem.nameResId)
                .create()
            binding.addButton.addActionItem(actionItem)
        }
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        setTitle(getString(R.string.source_list_screen_name))
    }

    override fun collectViewActions(): Observable<SourcesViewModel.Action> = Observable.merge(
        adapter.itemClick.map { SourcesViewModel.Action.OnListItemClicked(it) },
        binding.addButton.actionItemsClicks()
    )

    override fun handleEvent(event: SourcesViewModel.Event) {
        when (event) {
            SourcesViewModel.Event.CloseAddMenu -> binding.addButton.close(true)
        }
    }

    override fun render(state: SourcesViewModel.State) {
        adapter.onNewItems(state.listItems)
    }

    private fun SpeedDialView.actionItemsClicks(): Observable<SourcesViewModel.Action> {
        return Observable.create { emitter ->
            setOnActionSelectedListener { item ->
                emitter.onNext(SourcesViewModel.Action.OnAddMenuItemClicked(item.id))
                true
            }
        }
    }
}