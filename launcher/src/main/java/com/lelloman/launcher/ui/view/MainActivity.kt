package com.lelloman.launcher.ui.view

import android.os.Bundle
import android.view.ViewTreeObserver
import com.lelloman.common.utils.NavigationBarDetector
import com.lelloman.common.utils.model.Position
import com.lelloman.common.view.BaseActivity
import com.lelloman.launcher.R
import com.lelloman.launcher.databinding.ActivityMainBinding
import com.lelloman.launcher.packages.Package
import com.lelloman.launcher.ui.viewmodel.MainViewModel
import javax.inject.Inject

class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {

    override val layoutResId = R.layout.activity_main

    override val hasBaseLayout = false
    override val hasTransaprentNavigationBar = true

    @Inject
    lateinit var navigationBarDetector: NavigationBarDetector

    private val adapter by lazy { AppsDrawerAdapter(::onAppsDrawerElementClicked, resourceProvider) }

    override fun getViewModelClass() = MainViewModel::class.java

    private fun onAppsDrawerElementClicked(element: Any) = when (element) {
        is Package -> viewModel.onPackageClicked(element)
        else -> Unit
    }

    override fun setViewModel(binding: ActivityMainBinding, viewModel: MainViewModel) {
        binding.viewModel = viewModel
        val recyclerView = binding.appsDrawerView.recyclerView
        recyclerView.adapter = adapter
        viewModel.packages.observe(this, adapter)

        val navBarSpecs = navigationBarDetector.getNavigationBarSpecs()
        when (navBarSpecs.position) {
            Position.LEFT ->
                recyclerView.setPadding(navBarSpecs.width, 0, 0, 0)
            Position.BOTTOM ->
                recyclerView.setPadding(0, 0, 0, navBarSpecs.height - statusBarHeight)
            else -> Unit
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val globalLayoutLister = object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                window.decorView.rootView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                binding.bottomDrawerLayout.setDrawerFrameVisibility(true)
            }
        }
        window.decorView.rootView.viewTreeObserver.addOnGlobalLayoutListener(globalLayoutLister)
    }

    override fun onBackPressed() {
        // no super, this--is--launcher ┌∩┐(◣_◢)┌∩┐
        binding.bottomDrawerLayout.setMini()
    }
}
