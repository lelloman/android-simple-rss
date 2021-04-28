package com.lelloman.simplerss.ui_source.remote

import com.lelloman.simplerss.ui_base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

@HiltViewModel
class RemoteSourceViewModel @Inject constructor(
    private val interactor: RemoteSourceInteractor
) : BaseViewModel<RemoteSourceViewModel.State, RemoteSourceViewModel.Event, RemoteSourceViewModel.Action>() {

    override val defaultState = State()

    override fun createActionsMappers(actionsSource: Observable<Action>): List<Observable<Event>> {
        return listOf(Observable.empty())
    }

    data class State(val something: Boolean = true) : BaseViewModel.State

    sealed class Action : BaseViewModel.Action

    sealed class Event : BaseViewModel.Event
}