package com.lelloman.launcher.ui.main.view

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.lelloman.common.view.adapter.BaseMultiTypeRecyclerViewAdapter
import com.lelloman.common.view.adapter.ItemType
import com.lelloman.common.view.ResourceProvider
import com.lelloman.common.viewmodel.BaseListItemViewModel
import com.lelloman.launcher.R
import com.lelloman.launcher.databinding.ListItemPackageBinding
import com.lelloman.launcher.databinding.ListItemSearchBinding
import com.lelloman.launcher.ui.main.AppsDrawerListItem
import com.lelloman.launcher.ui.main.viewmodel.PackageDrawerListItem
import com.lelloman.launcher.ui.main.viewmodel.PackageListItemViewModel
import com.lelloman.launcher.ui.main.viewmodel.SearchDrawerListItem
import com.lelloman.launcher.ui.main.viewmodel.SearchListItemViewModel

class AppsDrawerAdapter(
    onClickListener: (Any) -> Unit,
    resourceProvider: ResourceProvider,
    onSearchQueryChanged: (String) -> Unit
) : BaseMultiTypeRecyclerViewAdapter<AppsDrawerListItem>(
    onClickListener = onClickListener,
    resourceProvider = resourceProvider
) {
    @Suppress("UNCHECKED_CAST")
    override val itemsMap: Map<Any, ItemType<AppsDrawerListItem, BaseListItemViewModel<AppsDrawerListItem>, ViewDataBinding>>
        get() {
            val out = mutableMapOf<Any, ItemType<AppsDrawerListItem, BaseListItemViewModel<AppsDrawerListItem>, ViewDataBinding>>()
            out[PackageDrawerListItem::class.java] = packageItem as ItemType<AppsDrawerListItem, BaseListItemViewModel<AppsDrawerListItem>, ViewDataBinding>
            out[SearchDrawerListItem::class.java] = searchItem as ItemType<AppsDrawerListItem, BaseListItemViewModel<AppsDrawerListItem>, ViewDataBinding>
            return out
        }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        recyclerView.layoutManager = GridLayoutManager(recyclerView.context, 5).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int) = if (data[position].requiresFullRow) {
                    5
                } else {
                    1
                }
            }
        }
    }

    private val packageItem = object : ItemType<PackageDrawerListItem, PackageListItemViewModel, ListItemPackageBinding> {
        override val ordinal = 1
        override fun createBinding(parent: ViewGroup): ListItemPackageBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.list_item_package,
            parent,
            false
        )

        override fun createViewModel(resourceProvider: ResourceProvider, onClickListener: ((Any) -> Unit)?) =
            PackageListItemViewModel()

        override fun bindViewModel(viewModel: PackageListItemViewModel, binding: ListItemPackageBinding, item: PackageDrawerListItem) {
            binding.viewModel = viewModel
        }
    }

    private val searchItem = object : ItemType<SearchDrawerListItem, SearchListItemViewModel, ListItemSearchBinding> {
        override val ordinal = 2
        override fun createBinding(parent: ViewGroup): ViewDataBinding {
            val binding: ListItemSearchBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.list_item_search,
                parent,
                false
            )
            val hint = binding.exitTextSearch.hint
            binding.exitTextSearch.setOnFocusChangeListener { _, hasFocus ->
                binding.exitTextSearch.hint = if (hasFocus) "" else hint
            }
            return binding
        }

        override fun createViewModel(resourceProvider: ResourceProvider, onClickListener: ((Any) -> Unit)?) = SearchListItemViewModel(onSearchQueryChanged)

        override fun bindViewModel(viewModel: SearchListItemViewModel, binding: ListItemSearchBinding, item: SearchDrawerListItem) {
            binding.viewModel = viewModel
        }
    }
}
