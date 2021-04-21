package com.lelloman.simplerss.ui_base

import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.jakewharton.rxbinding4.view.clicks
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import timber.log.Timber

abstract class BaseFragment<VMS : BaseViewModel.State, VME : BaseViewModel.Event, VMA : BaseViewModel.Action> :
    Fragment() {

    protected abstract val viewModel: BaseViewModel<VMS, VME, VMA>

    protected open val actionBar: Toolbar? = null

    private val appCompatActivity get() = requireActivity() as AppCompatActivity

    private var viewModelSubscription: Disposable? = null

    private var viewActionsSubscription: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelSubscription = viewModel
            .events
            .subscribe(::handleEvent, Timber::e)
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        appCompatActivity.setSupportActionBar(actionBar)
        viewModel.stateLiveData.observe(viewLifecycleOwner, ::render)
        viewActionsSubscription = collectViewActions()
            .subscribe(viewModel::processAction) {
                Timber.e(it, "Error processing action")
            }
    }

    protected abstract fun collectViewActions(): Observable<VMA>

    override fun onDestroyView() {
        super.onDestroyView()
        viewActionsSubscription?.dispose()
    }

    override fun onDestroy() {
        viewModelSubscription?.dispose()
        super.onDestroy()
    }

    protected abstract fun handleEvent(event: VME)

    protected abstract fun render(state: VMS)

    protected fun setTitle(title: String) {
        appCompatActivity.title = title
    }

    protected fun View.mapClicks(actionGenerator: () -> VMA): Observable<VMA> = clicks().map { actionGenerator() }
}