package com.lelloman.simplerss.ui.discover.viewmodel

import androidx.lifecycle.MutableLiveData
import com.lelloman.common.utils.LazyLiveData
import com.lelloman.simplerss.R
import com.lelloman.simplerss.feed.finder.FoundFeed
import com.lelloman.simplerss.persistence.db.SourcesDao
import com.lelloman.simplerss.persistence.db.model.Source
import com.lelloman.simplerss.ui.OpenAddFoundFeedsConfirmationScreenCommand
import com.lelloman.simplerss.ui.OpenAddSourceScreenCommand
import com.lelloman.simplerss.ui.common.repository.DiscoverRepository
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.BehaviorSubject

class FoundFeedListViewModelImpl(
    private val discoverRepository: DiscoverRepository,
    private val sourcesDao: SourcesDao,
    dependencies: Dependencies
) : FoundFeedListViewModel(dependencies) {

    private val foundFeedsCount = BehaviorSubject.createDefault(0)

    private var hasFoundAtLeastOneFeed = false

    override val isFindingFeeds: MutableLiveData<Boolean> by LazyLiveData {
        subscription {
            discoverRepository
                .isFindingFeeds
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
                .subscribe {
                    isFindingFeeds.postValue(it)
                    if (!it && foundFeedsCount.value == 0) {
                        navigateBack()
                        if (!hasFoundAtLeastOneFeed) {
                            longToast(getString(R.string.no_feed_found))
                        }
                    }
                }
        }
    }

    override val foundFeeds: MutableLiveData<List<FoundFeed>> by LazyLiveData {
        subscription {
            Observable
                .combineLatest(
                    discoverRepository.foundFeeds,
                    sourcesDao.getAll().toObservable(),
                    BiFunction<List<FoundFeed>, List<Source>, List<FoundFeed>> { foundFeeds, dbSources ->
                        foundFeeds.filter { foundFeed ->
                            !dbSources.any { it.url == foundFeed.url }
                        }
                    }
                )
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
                .doOnNext {
                    if (it.isNotEmpty()) hasFoundAtLeastOneFeed = true
                    foundFeeds.postValue(ArrayList(it))
                    foundFeedsCount.onNext(it.size)
                }
                .filter { it.isEmpty() }
                .withLatestFrom(
                    discoverRepository.isFindingFeeds,
                    BiFunction<List<FoundFeed>, Boolean, Boolean> { feeds, isLoading ->
                        feeds.isEmpty() && !isLoading
                    })
                .filter { it }
                .subscribe {
                    navigateBack()
                }
        }
    }

    override fun onFoundFeedClicked(foundFeed: FoundFeed) {
        emitCommand(
            OpenAddSourceScreenCommand(
                name = foundFeed.name ?: foundFeed.url,
                url = foundFeed.url
            )
        )
    }

    override fun onAddAllClicked() {
        foundFeeds
            .value
            ?.let { foundFeedsList ->
                val foundFeeds = foundFeedsList as? ArrayList ?: ArrayList(foundFeedsList)
                emitCommand(OpenAddFoundFeedsConfirmationScreenCommand(foundFeeds))
            }
    }

    override fun onAddAllFoundFeedsConfirmationClicked(foundFeeds: List<FoundFeed>) {
        discoverRepository
            .addFoundFeeds(foundFeeds)
            .subscribeOn(ioScheduler)
            .subscribe()
        navigateBack()
    }
}