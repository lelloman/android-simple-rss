package com.lelloman.read.ui.sources.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.view.View
import com.lelloman.read.R
import com.lelloman.read.core.ActionTokenProvider
import com.lelloman.read.core.navigation.NavigationScreen
import com.lelloman.read.core.view.ResourceProvider
import com.lelloman.read.core.view.actionevent.SnackEvent
import com.lelloman.read.persistence.db.model.Source
import com.lelloman.read.ui.common.repository.ArticlesRepository
import com.lelloman.read.ui.common.repository.DeletedSource
import com.lelloman.read.ui.common.repository.SourcesRepository
import com.lelloman.read.utils.LazyLiveData
import io.reactivex.Completable
import io.reactivex.Scheduler

class SourcesListViewModelImpl(
    private val ioScheduler: Scheduler,
    private val uiScheduler: Scheduler,
    private val sourcesRepository: SourcesRepository,
    private val articlesRepository: ArticlesRepository,
    resourceProvider: ResourceProvider,
    actionTokenProvider: ActionTokenProvider = ActionTokenProvider()
) : SourcesListViewModel(
    resourceProvider = resourceProvider,
    actionTokenProvider = actionTokenProvider
) {

    override val sources: MutableLiveData<List<Source>> by LazyLiveData {
        subscription {
            sourcesRepository.fetchSources()
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
                .subscribe { sources.value = it }
        }
    }

    private val deletedSourceMap: MutableMap<String, DeletedSource> = hashMapOf()

    override fun onTokenAction(token: String) {
        deletedSourceMap[token]?.let { deletedSource ->
            sourcesRepository
                .insertSource(deletedSource.source)
                .flatMap { sourceId ->
                    val articlesToInsert = deletedSource.articles
                        .map { it.copy(sourceId = sourceId) }
                    articlesRepository
                        .insertArticles(articlesToInsert)
                }
                .subscribeOn(ioScheduler)
                .subscribe(
                    {},
                    { shortToast(getString(R.string.something_went_wrong)) }
                )
        }
    }

    override fun onFabClicked(view: View) =
        navigate(NavigationScreen.ADD_SOURCE)

    override fun onSourceClicked(source: Source) {
        sourcesRepository
            .getSource(source.id)
            .firstOrError()
            .flatMapCompletable {
                Completable.fromAction {
                    onSourceIsActiveChanged(source.id, !it.isActive)
                }
            }
            .subscribeOn(ioScheduler)
            .observeOn(ioScheduler)
            .subscribe()

        // TODO show dedicated screen
        // navigate(ScreenNavigationEvent(NavigationScreen.SOURCE, arrayOf(sourceId)))
    }

    override fun onSourceIsActiveChanged(sourceId: Long, isActive: Boolean) {
        sourcesRepository
            .setSourceIsActive(sourceId, isActive)
            .subscribeOn(ioScheduler)
            .subscribe()
    }

    override fun onSourceSwiped(source: Source) {
        sourcesRepository
            .deleteSource(source)
            .subscribeOn(ioScheduler)
            .subscribe({
                val message = getString(R.string.source_deleted, source.name)
                val actionToken = makeActionToken()
                deletedSourceMap[actionToken] = it
                viewActionEvents.postValue(SnackEvent(
                    message = message,
                    actionLabel = getString(R.string.undo),
                    actionToken = actionToken
                ))
            }, {
                shortToast(getString(R.string.something_went_wrong))
            })
    }
}