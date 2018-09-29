package com.lelloman.launcher.ui

import android.support.v7.widget.GridLayoutManager
import com.lelloman.common.utils.NavigationBarDetector
import com.lelloman.common.utils.model.Position
import com.lelloman.common.view.BaseActivity
import com.lelloman.common.view.BaseRecyclerViewAdapter
import com.lelloman.launcher.R
import com.lelloman.launcher.databinding.ActivityMainBinding
import com.lelloman.launcher.databinding.ListItemPackageBinding
import com.lelloman.launcher.packages.Package
import javax.inject.Inject

class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {

    override val layoutResId = R.layout.activity_main

    override val hasBaseLayout = false
    override val hasTransaprentNavigationBar = true

    @Inject
    lateinit var navigationBarDetector: NavigationBarDetector

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

        val navBarSpecs = navigationBarDetector.getNavigationBarSpecs()
        when (navBarSpecs.position) {
            Position.LEFT ->
                binding.recyclerViewApps.setPadding(navBarSpecs.width, 0, 0, 0)
            Position.BOTTOM ->
                binding.recyclerViewApps.setPadding(0, 0, 0, navBarSpecs.height - statusBarHeight)
            else -> Unit
        }
    }

    override fun onBackPressed() {
        // no super, this--is--launcher ┌∩┐(◣_◢)┌∩┐
    }
}
