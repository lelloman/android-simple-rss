package com.lelloman.simplerss.ui_feed

import com.lelloman.simplerss.ui_base.BaseViewModel
import com.lelloman.simplerss.ui_feed.FeedViewModel.Action.AboutButtonClicked
import com.lelloman.simplerss.ui_feed.FeedViewModel.Action.SettingsButtonClicked
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val interactor: FeedInteractor
) : BaseViewModel<FeedViewModel.State, FeedViewModel.Event, FeedViewModel.Action>() {

    override val defaultState = State()

    override fun createActionsMappers(actionsSource: Observable<Action>) = listOf(
        actionsSource.aboutButtonClicked,
        actionsSource.eventlessIgnoredImmediate(SettingsButtonClicked::class, interactor::goToSettings)
    )

    private val Observable<Action>.aboutButtonClicked: Observable<Event>
        get() = ofType(AboutButtonClicked::class.java)
            .filter { !state.isLoading }
            .updateStateOnNext { it.copy(isLoading = true) }
            .flatMapSingle { clickedAction ->
                Single.timer(2, TimeUnit.SECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSuccess {
                        interactor.goToAbout()
                        interactor.goToAbout()
                    }
                    .doAfterTerminate { stateUpdate { it.copy(isLoading = false) } }
                    .map<Action> { clickedAction }
            }
            .noEvent()

    data class State(
        val isLoading: Boolean = false
    ) : BaseViewModel.State

    sealed class Event : BaseViewModel.Event

    sealed class Action : BaseViewModel.Action {
        object AboutButtonClicked : Action()
        object SettingsButtonClicked : Action()
    }
}