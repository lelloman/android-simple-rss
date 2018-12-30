package com.lelloman.launcher.ui.main.view

import android.support.design.widget.BottomSheetBehavior
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.lelloman.common.utils.NavigationBarDetector
import com.lelloman.common.utils.model.Position
import com.lelloman.common.view.BaseActivity
import com.lelloman.common.view.adapter.BaseRecyclerViewAdapter
import com.lelloman.launcher.R
import com.lelloman.launcher.databinding.ActivityMainBinding
import com.lelloman.launcher.databinding.ListItemClassifiedPackageBinding
import com.lelloman.launcher.ui.main.viewmodel.MainViewModel
import com.lelloman.launcher.ui.main.viewmodel.PackageDrawerListItem
import com.lelloman.launcher.ui.main.viewmodel.PackageListItemViewModel
import javax.inject.Inject

class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {

    override val layoutResId = R.layout.activity_main

    override val hasBaseLayout = false
    override val hasTransaprentNavigationBar = true

    @Inject
    lateinit var navigationBarDetector: NavigationBarDetector

    private val drawerAdapter by lazy {
        AppsDrawerAdapter(
            onClickListener = ::onAppsDrawerElementClicked,
            resourceProvider = resourceProvider,
            onSearchQueryChanged = viewModel::onSearchQueryChanged
        )
    }

    private val classifiedAdapter = object : BaseRecyclerViewAdapter<Long, PackageDrawerListItem, PackageListItemViewModel, ListItemClassifiedPackageBinding>(
        onItemClickListener = this@MainActivity::onAppsDrawerElementClicked
    ) {
        override val listItemLayoutResId = R.layout.list_item_classified_package

        override fun bindViewModel(binding: ListItemClassifiedPackageBinding, viewModel: PackageListItemViewModel) {
            binding.viewModel = viewModel
        }

        override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
            super.onAttachedToRecyclerView(recyclerView)
            recyclerView.layoutManager = object : GridLayoutManager(recyclerView.context, 5) {
                override fun canScrollVertically() = false
            }
        }

        override fun createViewModel(viewHolder: BaseViewHolder<Long, PackageDrawerListItem, PackageListItemViewModel, ListItemClassifiedPackageBinding>) = PackageListItemViewModel()
    }

    override fun getViewModelClass() = MainViewModel::class.java

    private fun onAppsDrawerElementClicked(element: Any) = when (element) {
        is PackageDrawerListItem -> viewModel.onPackageClicked(element.pkg)
        else -> Unit
    }

    override fun setViewModel(binding: ActivityMainBinding, viewModel: MainViewModel) {
        binding.viewModel = viewModel
        val drawerRecyclerView = binding.recyclerViewDrawer
        drawerRecyclerView.adapter = drawerAdapter
        binding.recyclerViewClassified.adapter = classifiedAdapter
        viewModel.drawerApps.observe(this, drawerAdapter)
        viewModel.classifiedApps.observe(this, classifiedAdapter)
        viewModel.homePages.observe(this, binding.homeView)

        val navBarSpecs = navigationBarDetector.getNavigationBarSpecs()
        when (navBarSpecs.position) {
            Position.LEFT ->
                drawerRecyclerView.setPadding(navBarSpecs.width, 0, 0, 0)
            Position.BOTTOM ->
                drawerRecyclerView.setPadding(0, 0, 0, navBarSpecs.height - statusBarHeight)
            else -> Unit
        }

        val headerView = binding.header
        val fullView = binding.recyclerViewDrawer
        bottomSheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(p0: View, inversePercent: Float) {
                val percent = 1 - inversePercent
                headerView.alpha = Math.pow(percent.toDouble(), 2.0).toFloat()
                fullView.alpha = 1f - percent

                val opened = percent <= 0f
                fullView.visibility = if (percent == 1f) View.GONE else View.VISIBLE
                headerView.visibility = if (opened) View.GONE else View.VISIBLE
            }

            override fun onStateChanged(view: View, state: Int) {
                when (state) {
                    BottomSheetBehavior.STATE_COLLAPSED -> binding.recyclerViewDrawer.scrollToPosition(0)
                }
            }
        })
    }

    private val bottomSheetBehavior by lazy { BottomSheetBehavior.from(binding.bottomSheet) }

    override fun onBackPressed() {
        // no super, this--is--launcher ┌∩┐(◣_◢)┌∩┐
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }
}
