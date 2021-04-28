package com.lelloman.simplerss.ui_source.list

import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import com.lelloman.simplerss.ui_base.BaseViewModel
import com.lelloman.simplerss.ui_source.R
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

@HiltViewModel
class SourcesViewModel @Inject constructor(
    private val interactor: SourcesInteractor
) : BaseViewModel<SourcesViewModel.State, SourcesViewModel.Event, SourcesViewModel.Action>() {

    val addMenuItems = listOf(
        AddMenuItem(
            nameResId = R.string.add_local_source_menu_action,
            iconResId = R.drawable.ic_local_source_type,
            actionId = R.id.add_local_source_action,
        ),
        AddMenuItem(
            nameResId = R.string.add_remote_source_menu_action,
            iconResId = R.drawable.ic_remote_source_type,
            actionId = R.id.add_remote_source_action,
        ),
        AddMenuItem(
            nameResId = R.string.discover_menu_action,
            iconResId = R.drawable.ic_discover,
            actionId = R.id.discover_action,
        )
    )

    override val defaultState = State()

    private val firstLoadedSubject = PublishSubject.create<Unit>()

    override fun createActionsMappers(actionsSource: Observable<Action>): List<Observable<Event>> {
        return listOf(
            firstLoad(),
            actionsSource.addMenuClicks()
        )
    }

    override fun onObserverFirstActive() {
        firstLoadedSubject.onNext(Unit)
    }

    private fun firstLoad(): Observable<Event> = firstLoadedSubject
        .filter { !state.isLoading }
        .updateStateOnNext { it.copy(isLoading = true) }
        .flatMap { interactor.observeSources() }
        .map { it.toListItems() }
        .doOnNext { listItems ->
            stateUpdate { state ->
                state.copy(isLoading = false, listItems = listItems)
            }
        }
        .noEvent()

    private fun List<SourcesInteractor.Source>.toListItems(): List<ListItem> = map { source ->
        ListItem(
            id = source.id,
            name = source.name
        )
    }

    private fun Observable<Action>.addMenuClicks(): Observable<Event> {
        return ofType(Action.OnAddMenuItemClicked::class.java)
            .map {
                when (it.actionId) {
                    R.id.add_local_source_action -> interactor.navigateToAddLocalSource()
                    R.id.add_remote_source_action -> interactor.navigateToAddRemoteSource()
                    R.id.discover_action -> interactor.navigateToDiscover()
                    else -> error("Unknown action id ${it.actionId}")
                }
                Event.CloseAddMenu
            }
    }

    data class State(
        val isLoading: Boolean = false,
        val listItems: List<ListItem> = emptyList(),
    ) : BaseViewModel.State

    sealed class Action : BaseViewModel.Action {
        data class OnListItemClicked(val item: ListItem) : Action()
        data class OnAddMenuItemClicked(val actionId: Int) : Action()
    }

    sealed class Event : BaseViewModel.Event {
        object CloseAddMenu : Event()
    }

    data class ListItem(
        val id: String,
        val name: String
    )

    data class AddMenuItem(
        @IdRes val actionId: Int,
        @DrawableRes val iconResId: Int,
        @StringRes val nameResId: Int
    )
}