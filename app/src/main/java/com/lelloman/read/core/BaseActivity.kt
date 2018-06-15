package com.lelloman.read.core

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.widget.Toast
import com.lelloman.read.core.navigation.NavigationEvent
import com.lelloman.read.core.view.ToastEvent

abstract class BaseActivity<VM : BaseViewModel, DB : ViewDataBinding>
    : InjectableActivity() {

    protected lateinit var viewModel: VM
    protected lateinit var binding: DB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, getLayoutId())
        binding.setLifecycleOwner(this)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(getViewModelClass())

        viewModel.viewActionEvents.observe(this, Observer {
            when (it) {
                is NavigationEvent -> navigationRouter.onNavigationEvent(this, it)
                is ToastEvent -> showToast(it)
            }
        })
    }

    private fun showToast(event: ToastEvent) {
        val text = getString(event.stringId, event.args)
        Toast.makeText(this, text, event.duration).show()
    }

    @LayoutRes
    protected abstract fun getLayoutId(): Int

    protected abstract fun getViewModelClass(): Class<VM>

}