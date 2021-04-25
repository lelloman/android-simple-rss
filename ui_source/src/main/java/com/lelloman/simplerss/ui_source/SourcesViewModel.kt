package com.lelloman.simplerss.ui_source

import com.lelloman.simplerss.ui_base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

@HiltViewModel
class SourcesViewModel @Inject constructor() :
    BaseViewModel<SourcesViewModel.State, SourcesViewModel.Event, SourcesViewModel.Action>() {

    override val defaultState = State()

    override fun createActionsMappers(actionsSource: Observable<Action>): List<Observable<Event>> {
        return emptyList()
    }

    data class State(val isLoading: Boolean = false) : BaseViewModel.State

    sealed class Action : BaseViewModel.Action

    sealed class Event : BaseViewModel.Event
}