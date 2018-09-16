package com.lelloman.common.viewmodel

import android.arch.lifecycle.ViewModel
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.widget.Toast
import com.lelloman.common.navigation.CloseScreenNavigationEvent
import com.lelloman.common.navigation.DeepLink
import com.lelloman.common.navigation.DeepLinkAndCloseNavigationEvent
import com.lelloman.common.navigation.DeepLinkNavigationEvent
import com.lelloman.common.navigation.NavigationEvent
import com.lelloman.common.navigation.NavigationScreen
import com.lelloman.common.utils.ActionTokenProvider
import com.lelloman.common.utils.SingleLiveData
import com.lelloman.common.view.ResourceProvider
import com.lelloman.common.view.actionevent.AnimationViewActionEvent
import com.lelloman.common.view.actionevent.SnackEvent
import com.lelloman.common.view.actionevent.ToastEvent
import com.lelloman.common.view.actionevent.ViewActionEvent
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

    protected fun navigate(deepLink: DeepLink) =
        viewActionEvents.postValue(DeepLinkNavigationEvent(deepLink))

    protected fun navigate(navigationScreen: NavigationScreen) = navigate(DeepLink(navigationScreen))

    protected fun navigateAndClose(navigationScreen: NavigationScreen) =
        viewActionEvents.postValue(DeepLinkAndCloseNavigationEvent(DeepLink(navigationScreen)))

    protected fun navigateBack() = navigate(CloseScreenNavigationEvent)

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