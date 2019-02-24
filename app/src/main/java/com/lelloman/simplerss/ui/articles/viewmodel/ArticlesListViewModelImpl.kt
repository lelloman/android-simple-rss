package com.lelloman.simplerss.ui.articles.viewmodel

import androidx.lifecycle.MutableLiveData
import com.lelloman.common.navigation.DeepLink
import com.lelloman.common.navigation.ViewIntentNavigationEvent
import com.lelloman.common.utils.LazyLiveData
import com.lelloman.simplerss.R
import com.lelloman.simplerss.navigation.SimpleRssNavigationScreen
import com.lelloman.simplerss.navigation.SimpleRssNavigationScreen.Companion.ARG_URL
import com.lelloman.simplerss.persistence.db.model.Source
import com.lelloman.simplerss.persistence.db.model.SourceArticle
import com.lelloman.simplerss.persistence.settings.AppSettings
import com.lelloman.simplerss.ui.common.repository.ArticlesRepository
import com.lelloman.simplerss.ui.common.repository.DiscoverRepository
import com.lelloman.simplerss.ui.common.repository.SourcesRepository
import io.reactivex.Observable

class ArticlesListViewModelImpl(
    private val articlesRepository: ArticlesRepository,
    private val sourcesRepository: SourcesRepository,
    private val discoverRepository: DiscoverRepository,
    private val appSettings: AppSettings,
    dependencies: Dependencies
) : ArticlesListViewModel(dependencies) {

    override val emptyViewVisible = MutableLiveData<Boolean>()
    override val emptyViewDescriptionText = MutableLiveData<String>()
    override val emptyViewButtonText = MutableLiveData<String>()

    private var emptyViewAction: (() -> Unit)? = null

    override val isLoading: MutableLiveData<Boolean> by LazyLiveData {
        subscription {
            articlesRepository.loading
                .distinctUntilChanged()
                .subscribeOn(ioScheduler)
                .subscribe { isLoading.postValue(it) }
        }
    }

    override val articles: MutableLiveData<List<SourceArticle>> by LazyLiveData {
        subscription {
            articlesRepository.fetchArticles()
                .flatMap { articles ->
                    if (articles.isEmpty()) {
                        sourcesRepository.fetchSources()
                            .first(emptyList())
                            .observeOn(uiScheduler)
                            .doOnSuccess(::setEmptyViewValues)
                            .map { articles }
                            .toObservable()
                    } else {
                        Observable.just(articles)
                    }
                }
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
                .doOnError { setNoArticleAvailableAtm() }
                .subscribe {
                    articles.postValue(it)
                    emptyViewVisible.postValue(it.isEmpty())
                }
        }
    }

    private val openArticlesInApp get() = appSettings.openArticlesInApp.blockingFirst()

    init {
        emptyViewVisible.value = false
    }

    override fun onViewShown() {
        super.onViewShown()
        discoverRepository.reset()
    }

    override fun refresh() = articlesRepository.refresh()

    override fun onSourcesClicked() = navigate(SimpleRssNavigationScreen.SOURCES_LIST)

    override fun onEmptyViewButtonClicked() {
        emptyViewAction?.invoke()
    }

    override fun onArticleClicked(article: SourceArticle) = if (openArticlesInApp) {
        navigate(
            DeepLink(SimpleRssNavigationScreen.ARTICLE)
                .putString(ARG_URL, article.link)
        )
    } else {
        navigate(ViewIntentNavigationEvent(article.link))
    }

    override fun onSettingsClicked() = navigate(SimpleRssNavigationScreen.SETTINGS)

    override fun onDiscoverSourceClicked() = navigate(SimpleRssNavigationScreen.DISCOVER_URL)

    override fun onDebugClicked() = shortToast("DEBUG")

    private fun setNoArticleAvailableAtm() {
        emptyViewDescriptionText.value = getString(R.string.empty_articles_must_refresh)
        emptyViewButtonText.value = getString(R.string.refresh)
        emptyViewAction = ::refresh
    }

    private fun setEmptyViewValues(sources: List<Source>) {
        when {
            sources.isEmpty() -> {
                emptyViewDescriptionText.value = getString(R.string.empty_articles_no_source)
                emptyViewButtonText.value = getString(R.string.add_source)
                emptyViewAction = { navigate(SimpleRssNavigationScreen.ADD_SOURCE) }
            }
            !sources.any(Source::isActive) -> {
                emptyViewDescriptionText.value = getString(R.string.empty_articles_sources_disabled)
                emptyViewButtonText.value = getString(R.string.enable_sources)
                emptyViewAction = { navigate(SimpleRssNavigationScreen.SOURCES_LIST) }
            }
            else -> setNoArticleAvailableAtm()
        }
    }
}
