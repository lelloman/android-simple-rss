package com.lelloman.read.core.view

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
import com.lelloman.read.R
import com.lelloman.read.core.navigation.NavigationEvent
import com.lelloman.read.core.view.actionevent.AnimationViewActionEvent
import com.lelloman.read.core.view.actionevent.SnackEvent
import com.lelloman.read.core.view.actionevent.SwipePageActionEvent
import com.lelloman.read.core.view.actionevent.ToastEvent
import com.lelloman.read.core.viewmodel.BaseViewModel
import io.reactivex.disposables.Disposable

abstract class BaseActivity<VM : BaseViewModel, DB : ViewDataBinding>
    : InjectableActivity() {

    protected lateinit var viewModel: VM
    protected lateinit var binding: DB

    private lateinit var coordinatorLayout: CoordinatorLayout

    private var hasSupportActionBarBackButton = false

    protected open val hasActionBar = true

    protected open val hasInverseTheme = false

    @LayoutRes
    protected open val layoutResId = 0

    private lateinit var themeChangesSubscription: Disposable

    private val logger by lazy { loggerFactory.getLogger(javaClass.simpleName) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val customTheme = appSettings
            .appTheme
            .blockingFirst()
        theme.applyStyle(customTheme.resId, true)

        themeChangesSubscription = appSettings
            .appTheme
            .filter { it != customTheme }
            .observeOn(uiScheduler)
            .subscribe {
                logger.d("Theme changed from $customTheme to $it")
                recreate()
            }

        if (hasInverseTheme) {
            theme.applyStyle(R.style.InverseTheme, true)
        }

        setContentView(R.layout.activity_base)
        setupActionBar()
        coordinatorLayout = findViewById(R.id.coordinator_layout)
        val layoutId = getLayoutId()
        binding = DataBindingUtil.inflate(layoutInflater, if (layoutId != 0) layoutId else layoutResId, coordinatorLayout, true)
        binding.setLifecycleOwner(this)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(getViewModelClass())

        viewModel.viewActionEvents.observe(this, Observer {
            when (it) {
                is NavigationEvent -> navigationRouter.onNavigationEvent(this, it)
                is ToastEvent -> showToast(it)
                is SnackEvent -> showSnack(it)
                is AnimationViewActionEvent -> onAnimationViewActionEvent(it)
                is SwipePageActionEvent -> onSwipePageActionEvent(it)
            }
        })

        viewModel.onCreate()
    }

    override fun onDestroy() {
        super.onDestroy()
        themeChangesSubscription.dispose()
    }

    private fun setupActionBar() {
        if (hasActionBar) {
            setSupportActionBar(findViewById(R.id.toolbar))
        } else {
            findViewById<AppBarLayout>(R.id.app_bar_layout).visibility = View.GONE
        }
    }

    protected open fun onAnimationViewActionEvent(animationViewActionEvent: AnimationViewActionEvent) {

    }

    protected open fun onSwipePageActionEvent(swipePageActionEvent: SwipePageActionEvent) {

    }

    protected fun setHasActionBarBackButton() {
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            hasSupportActionBarBackButton = true
        }
    }

    override fun onSupportNavigateUp() = if (hasSupportActionBarBackButton) {
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

    @LayoutRes
    @Deprecated("use layoutResId val instead instead", ReplaceWith("layoutResId"))
    protected open fun getLayoutId() = 0

    protected abstract fun getViewModelClass(): Class<VM>

}