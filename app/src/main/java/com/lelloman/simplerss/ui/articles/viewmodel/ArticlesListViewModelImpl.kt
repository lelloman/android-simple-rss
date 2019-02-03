package com.lelloman.simplerss.ui.articles.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.lelloman.common.navigation.DeepLink
import com.lelloman.common.navigation.ViewIntentNavigationEvent
import com.lelloman.common.utils.LazyLiveData
import com.lelloman.simplerss.R
import io.reactivex.Observable

class ArticlesListViewModelImpl(
    private val articlesRepository: com.lelloman.simplerss.ui.common.repository.ArticlesRepository,
    private val sourcesRepository: com.lelloman.simplerss.ui.common.repository.SourcesRepository,
    private val discoverRepository: com.lelloman.simplerss.ui.common.repository.DiscoverRepository,
    private val appSettings: com.lelloman.simplerss.persistence.settings.AppSettings,
    dependencies: Dependencies
) : com.lelloman.simplerss.ui.articles.viewmodel.ArticlesListViewModel(dependencies) {

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

    override val articles: MutableLiveData<List<com.lelloman.simplerss.persistence.db.model.SourceArticle>> by LazyLiveData {
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

    override fun onSourcesClicked() = navigate(com.lelloman.simplerss.navigation.SimpleRssNavigationScreen.SOURCES_LIST)

    override fun onEmptyViewButtonClicked() {
        emptyViewAction?.invoke()
    }

    override fun onArticleClicked(article: com.lelloman.simplerss.persistence.db.model.SourceArticle) = if (openArticlesInApp) {
        navigate(
            DeepLink(com.lelloman.simplerss.navigation.SimpleRssNavigationScreen.ARTICLE)
                .putString(com.lelloman.simplerss.navigation.SimpleRssNavigationScreen.ARG_URL, article.link)
        )
    } else {
        navigate(ViewIntentNavigationEvent(article.link))
    }

    override fun onSettingsClicked() = navigate(com.lelloman.simplerss.navigation.SimpleRssNavigationScreen.SETTINGS)

    override fun onDiscoverSourceClicked() = navigate(com.lelloman.simplerss.navigation.SimpleRssNavigationScreen.DISCOVER_URL)

    private fun setNoArticleAvailableAtm() {
        emptyViewDescriptionText.value = getString(R.string.empty_articles_must_refresh)
        emptyViewButtonText.value = getString(R.string.refresh)
        emptyViewAction = ::refresh
    }

    private fun setEmptyViewValues(sources: List<com.lelloman.simplerss.persistence.db.model.Source>) {
        when {
            sources.isEmpty() -> {
                emptyViewDescriptionText.value = getString(R.string.empty_articles_no_source)
                emptyViewButtonText.value = getString(R.string.add_source)
                emptyViewAction = { navigate(com.lelloman.simplerss.navigation.SimpleRssNavigationScreen.ADD_SOURCE) }
            }
            !sources.any(com.lelloman.simplerss.persistence.db.model.Source::isActive) -> {
                emptyViewDescriptionText.value = getString(R.string.empty_articles_sources_disabled)
                emptyViewButtonText.value = getString(R.string.enable_sources)
                emptyViewAction = { navigate(com.lelloman.simplerss.navigation.SimpleRssNavigationScreen.SOURCES_LIST) }
            }
            else -> setNoArticleAvailableAtm()
        }
    }
}
