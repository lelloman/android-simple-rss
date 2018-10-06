package com.lelloman.launcher.ui.launches.view

import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.lelloman.common.view.BaseActivity
import com.lelloman.common.view.BaseRecyclerViewAdapter
import com.lelloman.launcher.R
import com.lelloman.launcher.databinding.ActivityLaunchesBinding
import com.lelloman.launcher.databinding.ListItemPackageLaunchBinding
import com.lelloman.launcher.ui.launches.PackageLaunchListItem
import com.lelloman.launcher.ui.launches.viewmodel.LaunchesViewModel
import com.lelloman.launcher.ui.launches.viewmodel.PackageLaunchListItemViewModel

class LaunchesActivity : BaseActivity<LaunchesViewModel, ActivityLaunchesBinding>() {

    override val layoutResId = R.layout.activity_launches

    private val launchesAdapter = object : BaseRecyclerViewAdapter<PackageLaunchListItem, PackageLaunchListItemViewModel, ListItemPackageLaunchBinding>() {
        override val listItemLayoutResId = R.layout.list_item_package_launch

        override fun bindViewModel(binding: ListItemPackageLaunchBinding, viewModel: PackageLaunchListItemViewModel) {
            binding.viewModel = viewModel
        }

        override fun createViewModel(viewHolder: BaseViewHolder<PackageLaunchListItem, PackageLaunchListItemViewModel, ListItemPackageLaunchBinding>) =
            PackageLaunchListItemViewModel(semanticTimeProvider = semanticTimeProvider)
    }

    override fun setViewModel(binding: ActivityLaunchesBinding, viewModel: LaunchesViewModel) {
        binding.viewModel = viewModel
        binding.recyclerViewLaunches.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewLaunches.adapter = launchesAdapter
        viewModel.launches.observe(this, launchesAdapter)
    }

    override fun getViewModelClass() = LaunchesViewModel::class.java

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_launches, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_export -> {
            viewModel.onExportClicked()
            true
        }
        R.id.action_import -> {
            viewModel.onImportClicked()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}