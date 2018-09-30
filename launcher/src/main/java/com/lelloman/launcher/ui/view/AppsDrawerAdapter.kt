package com.lelloman.launcher.ui.view

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.lelloman.common.view.BaseMultiTypeRecyclerViewAdapter
import com.lelloman.common.view.ItemType
import com.lelloman.common.view.ResourceProvider
import com.lelloman.common.viewmodel.BaseListItemViewModel
import com.lelloman.launcher.R
import com.lelloman.launcher.databinding.ListItemPackageBinding
import com.lelloman.launcher.databinding.ListItemSearchBinding
import com.lelloman.launcher.ui.AppsDrawerListItem
import com.lelloman.launcher.ui.viewmodel.PackageDrawerListItem
import com.lelloman.launcher.ui.viewmodel.PackageListItemViewModel
import com.lelloman.launcher.ui.viewmodel.SearchDrawerListItem
import com.lelloman.launcher.ui.viewmodel.SearchListItemViewModel

class AppsDrawerAdapter(
    onClickListener: (Any) -> Unit,
    resourceProvider: ResourceProvider
) : BaseMultiTypeRecyclerViewAdapter<AppsDrawerListItem>(
    onClickListener = onClickListener,
    resourceProvider = resourceProvider
) {
    override val itemsMap: Map<Any, ItemType<AppsDrawerListItem, BaseListItemViewModel<AppsDrawerListItem>, ViewDataBinding>>
        get() {
            val out = mutableMapOf<Any, ItemType<AppsDrawerListItem, BaseListItemViewModel<AppsDrawerListItem>, ViewDataBinding>>()
            out[PackageDrawerListItem::class.java] = PackageItem as ItemType<AppsDrawerListItem, BaseListItemViewModel<AppsDrawerListItem>, ViewDataBinding>
            out[SearchDrawerListItem::class.java] = SearchItem as ItemType<AppsDrawerListItem, BaseListItemViewModel<AppsDrawerListItem>, ViewDataBinding>
            return out
        }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        val layoutManger = recyclerView.layoutManager as GridLayoutManager
        layoutManger.spanCount = 5
        layoutManger.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int) = when(position) {
                0 -> 5
                else -> 1
            }
        }
    }
}

private object PackageItem : ItemType<PackageDrawerListItem, PackageListItemViewModel, ListItemPackageBinding> {
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

private object SearchItem : ItemType<SearchDrawerListItem, SearchListItemViewModel, ListItemSearchBinding> {
    override val ordinal = 2
    override fun createBinding(parent: ViewGroup): ViewDataBinding  {
        val binding: ListItemSearchBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.list_item_search,
            parent,
            false
        )
        val hint = binding.exitTextSearch.hint
        binding.exitTextSearch.setOnFocusChangeListener { v, hasFocus ->
            binding.exitTextSearch.hint = if(hasFocus) "" else hint
        }
        return binding
    }

    override fun createViewModel(resourceProvider: ResourceProvider, onClickListener: ((Any) -> Unit)?) = SearchListItemViewModel()

    override fun bindViewModel(viewModel: SearchListItemViewModel, binding: ListItemSearchBinding, item: SearchDrawerListItem) {
        binding.viewModel = viewModel
    }
}