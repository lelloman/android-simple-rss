package com.lelloman.read.core.viewmodel

import android.arch.lifecycle.ViewModel
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.widget.Toast
import com.lelloman.read.core.ResourceProvider
import com.lelloman.read.core.navigation.BackNavigationEvent
import com.lelloman.read.core.navigation.NavigationEvent
import com.lelloman.read.core.view.SnackEvent
import com.lelloman.read.core.view.ToastEvent
import com.lelloman.read.core.view.ViewActionEvent
import com.lelloman.read.utils.SingleLiveData
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.util.*

abstract class BaseViewModel(
    private val resourceProvider: ResourceProvider
) : ViewModel() {

    private val subscriptions = CompositeDisposable()

    open val viewActionEvents = SingleLiveData<ViewActionEvent>()

    open fun onTokenAction(token: String) {

    }

    protected fun makeActionToken() = UUID.randomUUID().toString()

    protected fun getString(@StringRes stringId: Int, vararg args: Any = emptyArray()) =
        resourceProvider.getString(stringId, *args)

    protected fun navigate(navigationEvent: NavigationEvent) = viewActionEvents.postValue(navigationEvent)

    protected fun navigateBack() = navigate(BackNavigationEvent)

    protected fun shortToast(message: String) =
        viewActionEvents.postValue(ToastEvent(
            message = message,
            duration = Toast.LENGTH_SHORT
        ))

    protected fun longSnack(
        message: String,
        actionLabel: String? = null,
        actionToken: String? = null
    ) {
        viewActionEvents.postValue(SnackEvent(
            message = message,
            actionLabel = actionLabel,
            actionToken = actionToken,
            duration = Snackbar.LENGTH_LONG
        ))
    }

    protected fun subscription(action: () -> Disposable) {
        subscriptions.add(action.invoke())
    }

    override fun onCleared() {
        super.onCleared()
        subscriptions.dispose()
    }
}