package com.lelloman.read.core

import android.arch.lifecycle.ViewModel
import com.lelloman.read.core.navigation.NavigationEvent
import com.lelloman.read.utils.SingleLiveData
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseViewModel : ViewModel() {

    private val subscriptions = CompositeDisposable()

    open val navigation = SingleLiveData<NavigationEvent>()

    protected fun navigate(navigationEvent: NavigationEvent) = navigation.postValue(navigationEvent)

    protected fun subscription(action: () -> Disposable) {
        subscriptions.add(action.invoke())
    }

    override fun onCleared() {
        super.onCleared()
        subscriptions.dispose()
    }
}