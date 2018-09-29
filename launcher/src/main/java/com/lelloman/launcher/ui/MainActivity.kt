package com.lelloman.launcher.ui

import android.support.v7.widget.GridLayoutManager
import com.lelloman.common.view.BaseActivity
import com.lelloman.common.view.BaseRecyclerViewAdapter
import com.lelloman.launcher.R
import com.lelloman.launcher.databinding.ActivityMainBinding
import com.lelloman.launcher.databinding.ListItemPackageBinding
import com.lelloman.launcher.packages.Package

class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {

    override val layoutResId = R.layout.activity_main

    override val hasBaseLayout = false

    private val adapter = object : BaseRecyclerViewAdapter<Package, PackageListItemViewModel, ListItemPackageBinding>(
        onItemClickListener = { viewModel.onPackageClicked(it) }
    ) {

        override val listItemLayoutResId = R.layout.list_item_package

        override fun bindViewModel(binding: ListItemPackageBinding, viewModel: PackageListItemViewModel) {
            binding.viewModel = viewModel
        }

        override fun createViewModel(viewHolder: BaseViewHolder<Package, PackageListItemViewModel, ListItemPackageBinding>) = PackageListItemViewModel()
    }

    override fun getViewModelClass() = MainViewModel::class.java

    override fun setViewModel(binding: ActivityMainBinding, viewModel: MainViewModel) {
        binding.viewModel = viewModel
        binding.recyclerViewApps.layoutManager = GridLayoutManager(
            this,
            5
        )
        binding.recyclerViewApps.adapter = adapter
        viewModel.packages.observe(this, adapter)
    }

    override fun onBackPressed() {
        // no super, this--is--launcher ┌∩┐(◣_◢)┌∩┐
    }
}
