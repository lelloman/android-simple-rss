package com.lelloman.launcher.ui.view

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.view.LayoutInflater
import android.view.ViewGroup
import com.lelloman.common.view.BaseMultiTypeRecyclerViewAdapter
import com.lelloman.common.view.ItemType
import com.lelloman.common.view.ResourceProvider
import com.lelloman.common.viewmodel.BaseListItemViewModel
import com.lelloman.launcher.R
import com.lelloman.launcher.databinding.ListItemPackageBinding
import com.lelloman.launcher.packages.Package
import com.lelloman.launcher.ui.AppsDrawerListItem
import com.lelloman.launcher.ui.viewmodel.PackageDrawerListItem
import com.lelloman.launcher.ui.viewmodel.PackageListItemViewModel

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
            return out
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