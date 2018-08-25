package com.lelloman.read.core.viewmodel

import android.arch.lifecycle.ViewModel
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.widget.Toast
import com.lelloman.read.core.ActionTokenProvider
import com.lelloman.read.core.ResourceProvider
import com.lelloman.read.core.navigation.CloseScreenNavigationEvent
import com.lelloman.read.core.navigation.NavigationEvent
import com.lelloman.read.core.navigation.NavigationScreen
import com.lelloman.read.core.navigation.ScreenNavigationEvent
import com.lelloman.read.core.view.AnimationViewActionEvent
import com.lelloman.read.core.view.SnackEvent
import com.lelloman.read.core.view.ToastEvent
import com.lelloman.read.core.view.ViewActionEvent
import com.lelloman.read.utils.SingleLiveData
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseViewModel(
    private val resourceProvider: ResourceProvider,
    private val actionTokenProvider: ActionTokenProvider = ActionTokenProvider()
) : ViewModel() {

    private val subscriptions = CompositeDisposable()

    open val viewActionEvents = SingleLiveData<ViewActionEvent>()

    open fun onTokenAction(token: String) = Unit

    open fun onCreate() = Unit

    open fun onSaveInstanceState(bundle: Bundle) = Unit

    open fun onRestoreInstanceState(bundle: Bundle) = Unit

    protected fun makeActionToken() = actionTokenProvider.makeActionToken()

    protected fun getString(@StringRes stringId: Int, vararg args: Any = emptyArray()) =
        resourceProvider.getString(stringId, *args)

    protected fun navigate(navigationEvent: NavigationEvent) = viewActionEvents.postValue(navigationEvent)

    protected fun navigateBack() = navigate(CloseScreenNavigationEvent)

    protected fun navigateToScreen(screen: NavigationScreen, vararg args: Any) = navigate(
        ScreenNavigationEvent(screen, args)
    )

    protected fun animate(animationViewActionEvent: AnimationViewActionEvent) {
        viewActionEvents.postValue(animationViewActionEvent)
    }

    open fun onCloseClicked() = navigateBack()

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