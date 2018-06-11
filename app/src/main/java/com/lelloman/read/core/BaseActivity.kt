package com.lelloman.read.core

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.annotation.LayoutRes

abstract class BaseActivity<VM : BaseViewModel, DB : ViewDataBinding>
    : InjectableActivity() {

    protected lateinit var viewModel: VM
    protected lateinit var binding: DB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, getLayoutId())
        binding.setLifecycleOwner(this)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(getViewModelClass())

        viewModel.navigation.observe(this, Observer {
            it?.let {
                navigationRouter.onNavigationEvent(this, it)
            }
        })
    }

    @LayoutRes
    protected abstract fun getLayoutId(): Int

    protected abstract fun getViewModelClass(): Class<VM>

}