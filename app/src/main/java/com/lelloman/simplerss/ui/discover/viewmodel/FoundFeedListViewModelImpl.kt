package com.lelloman.simplerss.ui.discover.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.lelloman.common.navigation.DeepLink
import com.lelloman.common.utils.LazyLiveData
import com.lelloman.simplerss.R
import com.lelloman.simplerss.feed.finder.FoundFeed
import com.lelloman.simplerss.navigation.SimpleRssNavigationScreen
import com.lelloman.simplerss.navigation.SimpleRssNavigationScreen.Companion.ARG_FOUND_FEEDS
import com.lelloman.simplerss.navigation.SimpleRssNavigationScreen.Companion.ARG_SOURCE_NAME
import com.lelloman.simplerss.navigation.SimpleRssNavigationScreen.Companion.ARG_SOURCE_URL
import com.lelloman.simplerss.persistence.db.SourcesDao
import com.lelloman.simplerss.persistence.db.model.Source
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
                .withLatestFrom(discoverRepository.isFindingFeeds, BiFunction<List<FoundFeed>, Boolean, Boolean> { feeds, isLoading ->
                    feeds.isEmpty() && !isLoading
                })
                .filter { it }
                .subscribe {
                    navigateBack()
                }
        }
    }

    override fun onFoundFeedClicked(foundFeed: FoundFeed) {
        navigate(
            DeepLink(SimpleRssNavigationScreen.ADD_SOURCE)
                .putString(ARG_SOURCE_NAME, foundFeed.name ?: foundFeed.url)
                .putString(ARG_SOURCE_URL, foundFeed.url)
        )
    }

    override fun onAddAllClicked() {
        foundFeeds
            .value
            ?.let { foundFeedsList ->
                val foundFeeds = foundFeedsList as? ArrayList ?: ArrayList(foundFeedsList)
                navigate(
                    DeepLink(SimpleRssNavigationScreen.ADD_FOUND_FEEDS_CONFIRMATION)
                        .putSerializableArrayList(ARG_FOUND_FEEDS, foundFeeds)
                )
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