package com.lelloman.common.view

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar
import android.view.View
import android.widget.Toast
import com.lelloman.common.R
import com.lelloman.common.navigation.NavigationEvent
import com.lelloman.common.view.actionevent.AnimationViewActionEvent
import com.lelloman.common.view.actionevent.SnackEvent
import com.lelloman.common.view.actionevent.SwipePageActionEvent
import com.lelloman.common.view.actionevent.ThemeChangedActionEvent
import com.lelloman.common.view.actionevent.ToastEvent
import com.lelloman.common.viewmodel.BaseViewModel

abstract class BaseActivity<VM : BaseViewModel, DB : ViewDataBinding>
    : InjectableActivity() {

    protected lateinit var viewModel: VM
    protected lateinit var binding: DB

    private lateinit var coordinatorLayout: CoordinatorLayout

    protected open val hasActionBar = true
    protected open val hasInverseTheme = false
    protected open val hasBaseLayout = true
    protected open val hasActionBarBackButton = false
    protected open val hasTransaprentNavigationBar = false

    @LayoutRes
    protected open val layoutResId = 0

    private val logger by lazy { loggerFactory.getLogger(javaClass.simpleName) }

    private val statusBarHeight: Int
        get() {
            var result = 0
            val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
            if (resourceId > 0) {
                result = resources.getDimensionPixelSize(resourceId)
            }
            return result
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(getViewModelClass())
        viewModel.onSetupTheme {
            theme.applyStyle(it.resId, true)
        }
        if (hasInverseTheme) {
            theme.applyStyle(R.style.InverseTheme, true)
        }
        if (hasTransaprentNavigationBar) {
            theme.applyStyle(R.style.TransparentNavigationBar, true)
        }

        if (hasBaseLayout) {
            setContentView(R.layout.activity_base)
            setupActionBar()
            coordinatorLayout = findViewById(R.id.coordinator_layout)
            binding = DataBindingUtil.inflate(layoutInflater, layoutResId, coordinatorLayout, true)
        } else {
            binding = DataBindingUtil.setContentView(this, layoutResId)
        }
        binding.setLifecycleOwner(this)

        if (hasTransaprentNavigationBar) {
            findViewById<View>(android.R.id.content).apply {
                setPadding(paddingLeft, statusBarHeight, paddingRight, paddingBottom)
            }
        }

        viewModel.viewActionEvents.observe(this, Observer {
            when (it) {
                is NavigationEvent -> navigationRouter.onNavigationEvent(this, it)
                is ToastEvent -> showToast(it)
                is SnackEvent -> showSnack(it)
                is AnimationViewActionEvent -> onAnimationViewActionEvent(it)
                is SwipePageActionEvent -> onSwipePageActionEvent(it)
                is ThemeChangedActionEvent -> recreate()
            }
        })
        viewModel.onCreate()
        setViewModel(binding, viewModel)
    }

    protected abstract fun setViewModel(binding: DB, viewModel: VM)

    private fun setupActionBar() {
        if (hasActionBar) {
            setSupportActionBar(findViewById(R.id.toolbar))
        } else {
            findViewById<AppBarLayout>(R.id.app_bar_layout).visibility = View.GONE
        }

        if (hasActionBarBackButton) {
            supportActionBar?.apply {
                setDisplayHomeAsUpEnabled(true)
                setDisplayShowHomeEnabled(true)
            }
        }
    }

    protected open fun onAnimationViewActionEvent(animationViewActionEvent: AnimationViewActionEvent) {

    }

    protected open fun onSwipePageActionEvent(swipePageActionEvent: SwipePageActionEvent) {

    }

    override fun onSupportNavigateUp() = if (hasActionBarBackButton) {
        onBackPressed()
        true
    } else {
        super.onSupportNavigateUp()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        viewModel.onRestoreInstanceState(savedInstanceState)
    }

    private fun showToast(event: ToastEvent) {
        Toast.makeText(this, event.message, event.duration).show()
    }

    private fun showSnack(event: SnackEvent) {
        val snack = Snackbar.make(coordinatorLayout, event.message, event.duration)
        if (event.hasAction) {
            snack.setAction(event.actionLabel) { _ ->
                viewModel.onTokenAction(event.actionToken!!)
            }
        }
        snack.show()
    }

    protected abstract fun getViewModelClass(): Class<VM>

}