package com.lelloman.simplerss.ui.sources.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.view.View
import com.lelloman.common.utils.LazyLiveData
import com.lelloman.simplerss.R
import com.lelloman.simplerss.navigation.SimpleRssNavigationScreen
import com.lelloman.simplerss.persistence.db.model.Source
import com.lelloman.simplerss.ui.common.repository.ArticlesRepository
import com.lelloman.simplerss.ui.common.repository.DeletedSource
import com.lelloman.simplerss.ui.common.repository.SourcesRepository
import io.reactivex.Completable

class SourcesListViewModelImpl(
    private val sourcesRepository: SourcesRepository,
    private val articlesRepository: ArticlesRepository,
    dependencies: Dependencies
) : SourcesListViewModel(dependencies) {

    private val mutableEmptyViewVisible: MutableLiveData<Boolean> by LazyLiveData {
        mutableEmptyViewVisible.postValue(false)
    }

    override val emptyViewVisible: LiveData<Boolean> = mutableEmptyViewVisible

    override val sources: MutableLiveData<List<Source>> by LazyLiveData {
        subscription {
            sourcesRepository.fetchSources()
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
                .doOnNext { mutableEmptyViewVisible.postValue(it.isEmpty()) }
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
        navigate(SimpleRssNavigationScreen.ADD_SOURCE)

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
        subscription {
            sourcesRepository
                .deleteSource(source)
                .subscribeOn(ioScheduler)
                .subscribe({
                    val message = getString(R.string.source_deleted, source.name)
                    val actionToken = makeActionToken()
                    deletedSourceMap[actionToken] = it
                    longSnack(
                        message = message,
                        actionLabel = getString(R.string.undo),
                        actionToken = actionToken
                    )
                }, {
                    shortToast(getString(R.string.something_went_wrong))
                })
        }
    }
}