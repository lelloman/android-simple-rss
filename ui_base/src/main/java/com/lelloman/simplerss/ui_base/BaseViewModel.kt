package com.lelloman.simplerss.ui_base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlin.reflect.KClass

abstract class BaseViewModel<VMS : BaseViewModel.State, VME : BaseViewModel.Event, VMA : BaseViewModel.Action> :
    ViewModel() {

    protected abstract val defaultState: VMS

    private val mutableState by lazy { StateLiveData() }
    protected val state get() = mutableState.value!!
    val stateLiveData: LiveData<VMS> get() = mutableState

    private var isAwaitingFirstObserver = true

    private val actionsSubject = PublishSubject.create<VMA>()
    private val actionsSource: Observable<VMA>
        get() = actionsSubject
            .publish()
            .autoConnect()

    val events: Observable<VME> by lazy { buildActionsMapper(actionsSource) }

    protected fun stateUpdate(action: (VMS) -> VMS) {
        mutableState.value = action(mutableState.value!!)
    }

    fun processAction(action: VMA) {
        actionsSubject.onNext(action)
    }

    private fun buildActionsMapper(actionsSource: Observable<VMA>): Observable<VME> {
        return Observable.merge(createActionsMappers(actionsSource))
            .onErrorResumeNext { buildActionsMapper(actionsSource) }
    }

    protected abstract fun createActionsMappers(actionsSource: Observable<VMA>) : List<Observable<VME>>

    protected fun <T> Observable<T>.noEvent(): Observable<VME> = flatMap { Observable.empty() }

    protected fun <T : VMA> Observable<VMA>.eventlessIgnoredImmediate(
        kclass: KClass<T>,
        action: () -> Unit
    ): Observable<VME> = ofType(kclass.java)
        .doOnNext { action() }
        .noEvent()

    protected fun <T> Observable<T>.updateStateOnNext(updateAction: (VMS) -> VMS): Observable<T> =
        doOnNext { this@BaseViewModel.stateUpdate(updateAction) }

    protected fun <T> Single<T>.updateStateAfterTerminate(updateAction: (VMS) -> VMS): Single<T> =
        doAfterTerminate {
            this@BaseViewModel.stateUpdate(updateAction)
        }

    protected fun <T> Observable<T>.updateStateAfterTerminate(updateAction: (VMS) -> VMS): Observable<T> =
        doAfterTerminate {
            this@BaseViewModel.stateUpdate(updateAction)
        }

    protected fun <T> Observable<T>.updateStateOnError(updateAction: (VMS) -> VMS): Observable<T> =
        doOnError {
            this@BaseViewModel.stateUpdate(updateAction)
        }

    protected open fun onObserverFirstActive() = Unit

    interface State

    interface Event

    interface Action

    private inner class StateLiveData : MutableLiveData<VMS>(defaultState) {
        override fun onActive() {
            super.onActive()
            if (isAwaitingFirstObserver) {
                isAwaitingFirstObserver = false
                onObserverFirstActive()
            }
        }
    }
}