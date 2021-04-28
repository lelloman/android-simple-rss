package com.lelloman.simplerss.ui_source.local

import com.lelloman.simplerss.ui_base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

@HiltViewModel
class LocalSourceViewModel @Inject constructor(
    private val interactor: LocalSourceInteractor
) : BaseViewModel<LocalSourceViewModel.State, LocalSourceViewModel.Event, LocalSourceViewModel.Action>() {

    override val defaultState = State()

    override fun createActionsMappers(actionsSource: Observable<Action>): List<Observable<Event>> {
        return listOf(Observable.never(), Observable.never())
    }

    data class State(val something: Boolean = true) : BaseViewModel.State

    sealed class Action : BaseViewModel.Action

    sealed class Event : BaseViewModel.Event
}