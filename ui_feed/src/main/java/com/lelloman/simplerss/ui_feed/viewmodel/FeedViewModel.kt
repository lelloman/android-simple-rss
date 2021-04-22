package com.lelloman.simplerss.ui_feed.viewmodel

import com.lelloman.simplerss.ui_base.BaseViewModel
import com.lelloman.simplerss.ui_base.IoScheduler
import com.lelloman.simplerss.ui_base.UiScheduler
import com.lelloman.simplerss.ui_feed.model.FeedInteractor
import com.lelloman.simplerss.ui_feed.viewmodel.FeedViewModel.Action.AboutButtonClicked
import com.lelloman.simplerss.ui_feed.viewmodel.FeedViewModel.Action.SettingsButtonClicked
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val interactor: FeedInteractor,
    @IoScheduler private val ioScheduler: Scheduler,
    @UiScheduler private val uiScheduler: Scheduler
) : BaseViewModel<FeedViewModel.State, FeedViewModel.Event, FeedViewModel.Action>() {

    override val defaultState = State()

    private val firstLoadedSubject = PublishSubject.create<Unit>()

    override fun createActionsMappers(actionsSource: Observable<Action>) = listOf(
        actionsSource.eventlessIgnoredImmediate(AboutButtonClicked::class, interactor::goToAbout),
        actionsSource.eventlessIgnoredImmediate(SettingsButtonClicked::class, interactor::goToSettings),
        actionsSource.pullToRefresh(),
        firstLoad()
    )

    override fun onObserverFirstActive() {
        firstLoadedSubject.onNext(Unit)
    }

    private fun Observable<Action>.pullToRefresh(): Observable<Event> = Observable
        .merge(ofType(Action.FeedPulledToRefresh::class.java), ofType(Action.RefreshButtonClicked::class.java))
        .loadFeed { copy(isRefreshing = it) }

    private fun firstLoad(): Observable<Event> = firstLoadedSubject.loadFeed { copy(isLoadingFeed = it) }

    private fun <T> Observable<T>.loadFeed(loadingSetter: State.(Boolean) -> State): Observable<Event> =
        filter { !state.isLoadingFeed && !state.isRefreshing }
            .updateStateOnNext { it.loadingSetter(true) }
            .flatMapSingle {
                interactor.loadFeed()
                    .subscribeOn(ioScheduler)
            }
            .observeOn(uiScheduler)
            .doOnNext { items ->
                stateUpdate {
                    it.loadingSetter(false).copy(feedItems = items.map(::FeedListItem))
                }
            }
            .updateStateOnError {
                it.loadingSetter(false)
            }
            .onErrorComplete()
            .noEvent()

    data class State(
        val isLoadingFeed: Boolean = false,
        val isRefreshing: Boolean = false,
        val feedItems: List<FeedListItem> = emptyList()
    ) : BaseViewModel.State

    sealed class Event : BaseViewModel.Event

    sealed class Action : BaseViewModel.Action {

        object AboutButtonClicked : Action()

        object SettingsButtonClicked : Action()

        object RefreshButtonClicked : Action()

        object SourcesButtonClicked : Action()

        object DiscoverButtonClicked : Action()

        object FeedPulledToRefresh : Action()

        data class FeedItemClicked(val item: FeedListItem) : Action()
    }

    data class FeedListItem(
        private val wrapped: FeedInteractor.FeedItem
    ) : FeedInteractor.FeedItem by wrapped
}