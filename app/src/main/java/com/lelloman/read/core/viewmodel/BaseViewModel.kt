package com.lelloman.read.core.viewmodel

import android.arch.lifecycle.ViewModel
import android.support.annotation.StringRes
import android.widget.Toast
import com.lelloman.read.core.ResourceProvider
import com.lelloman.read.core.navigation.BackNavigationEvent
import com.lelloman.read.core.navigation.NavigationEvent
import com.lelloman.read.core.view.ToastEvent
import com.lelloman.read.core.view.ViewActionEvent
import com.lelloman.read.utils.SingleLiveData
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseViewModel(
    private val resourceProvider: ResourceProvider
) : ViewModel() {

    private val subscriptions = CompositeDisposable()

    open val viewActionEvents = SingleLiveData<ViewActionEvent>()

    protected fun getString(@StringRes stringId: Int, args: Array<Any> = emptyArray()) =
        resourceProvider.getString(stringId, args)

    protected fun navigate(navigationEvent: NavigationEvent) = viewActionEvents.postValue(navigationEvent)

    protected fun navigateBack() = navigate(BackNavigationEvent)

    protected fun shortToast(@StringRes stringId: Int, args: Array<Any> = emptyArray()) =
        viewActionEvents.postValue(ToastEvent(
            stringId = stringId,
            args = args,
            duration = Toast.LENGTH_SHORT
        ))

    protected fun subscription(action: () -> Disposable) {
        subscriptions.add(action.invoke())
    }

    override fun onCleared() {
        super.onCleared()
        subscriptions.dispose()
    }
}