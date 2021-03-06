package com.lelloman.simplerss.ui.articles.viewmodel

import com.lelloman.common.jvmtestutils.AndroidArchTest
import com.lelloman.common.jvmtestutils.MockLoggerFactory
import com.lelloman.common.jvmtestutils.MockResourceProvider
import com.lelloman.common.jvmtestutils.test
import com.lelloman.common.viewmodel.BaseViewModel
import com.lelloman.common.viewmodel.command.ViewIntentNavigationEvent
import com.lelloman.simplerss.R
import com.lelloman.simplerss.mock.MockAppSettings
import com.lelloman.simplerss.persistence.db.model.Source
import com.lelloman.simplerss.persistence.db.model.SourceArticle
import com.lelloman.simplerss.testutils.dummySourceArticle
import com.lelloman.simplerss.ui.*
import com.lelloman.simplerss.ui.common.repository.ArticlesRepository
import com.lelloman.simplerss.ui.common.repository.DiscoverRepository
import com.lelloman.simplerss.ui.common.repository.SourcesRepository
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers.trampoline
import io.reactivex.subjects.BehaviorSubject
import org.junit.Test

class ArticlesListViewModelImplTest : AndroidArchTest() {

    private val articlesRepository: ArticlesRepository = mock()
    private val sourcesRepository: SourcesRepository = mock()
    private val discoveryRepository: DiscoverRepository = mock()
    private val resourceProvider = MockResourceProvider()
    private val appSettings = MockAppSettings()

    private lateinit var tested: ArticlesListViewModelImpl

    override fun setUp() {
        tested = ArticlesListViewModelImpl(
            articlesRepository = articlesRepository,
            sourcesRepository = sourcesRepository,
            discoverRepository = discoveryRepository,
            appSettings = appSettings,
            dependencies = BaseViewModel.Dependencies(
                settings = appSettings,
                resourceProvider = resourceProvider,
                actionTokenProvider = mock(),
                ioScheduler = trampoline(),
                uiScheduler = trampoline(),
                loggerFactory = MockLoggerFactory()
            )
        )
    }

    @Test
    fun `triggers articles refresh`() {
        tested.refresh()

        verify(articlesRepository).refresh()
    }

    @Test
    fun `navigates to sources list when click on sources button`() {
        val tester = tested.commands.test()

        tested.onSourcesClicked()

        tester.assertValueCount(1)
        tester.assertValueAt(0) { it == OpenSourcesListScreenCommand }
    }

    @Test
    fun `navigates to article screen when article is clicked and open in app setting is true`() {
        givenOpenArticleInAppSettingEnabled()
        val tester = tested.commands.test()
        val link = "www.meow.com"
        val article = dummySourceArticle().copy(link = link)

        tested.onArticleClicked(article)

        tester.assertValueCount(1)
        tester.assertValueAt(0) { it == OpenArticleScreenCommand(link) }
    }

    @Test
    fun `navigates to view intent when article is clicked and open in app setting is false`() {
        val link = "asdasd"
        givenOpenArticleInAppSettingDisabled()
        val viewActions = tested.commands.test()
        val article = dummySourceArticle().copy(link = link)

        tested.onArticleClicked(article)

        viewActions.assertValues(ViewIntentNavigationEvent(link))
    }

    @Test
    fun `navigates to settings screen when settings button is clicked`() {
        val tester = tested.commands.test()

        tested.onSettingsClicked()

        tester.assertValueCount(1)
        tester.assertValueAt(0) { it == OpenSettingsScreenCommand }
    }

    @Test
    fun `emits articles repository loading state`() {
        val loadingSubject = BehaviorSubject.create<Boolean>()
        whenever(articlesRepository.loading).thenReturn(loadingSubject)
        loadingSubject.onNext(false)
        val tester = tested.isLoading.test()

        tester.assertValues(false)

        loadingSubject.onNext(true)
        tester.assertValues(false, true)

        loadingSubject.onNext(true)
        tester.assertValues(false, true)

        loadingSubject.onNext(false)
        tester.assertValues(false, true, false)
    }

    @Test
    fun `hides empty view and shows articles if articles list is not empty`() {
        givenHasArticles()

        val articlesTester = tested.articles.test()
        val emptyViewTester = tested.emptyViewVisible.test()

        emptyViewTester.assertValues(false)
        articlesTester.assertValues(ARTICLES)
    }

    @Test
    fun `shows empty view with action if it has no articles and no sources`() {
        givenNoArticles()
        givenNoSources()
        val emptyViewTester = tested.emptyViewVisible.test()
        val emptyTextTester = tested.emptyViewDescriptionText.test()
        val emptyButtonTester = tested.emptyViewButtonText.test()
        val viewActionsTester = tested.commands.test()

        val articlesTester = tested.articles.test()
        tested.onEmptyViewButtonClicked()

        emptyViewTester.assertValues(false, true)
        articlesTester.assertValues(emptyList())
        emptyTextTester.assertValues("${R.string.empty_articles_no_source}")
        emptyButtonTester.assertValues("${R.string.add_source}")
        viewActionsTester.assertValueCount(1)
        viewActionsTester.assertValueAt(0) { it == OpenAddSourceScreenCommand() }
    }

    @Test
    fun `shows empty view with action if it has no articles and no active source`() {
        givenNoArticles()
        givenNoActiveSources()
        val emptyViewTester = tested.emptyViewVisible.test()
        val emptyTextTester = tested.emptyViewDescriptionText.test()
        val emptyButtonTester = tested.emptyViewButtonText.test()
        val viewActionsTester = tested.commands.test()

        val articlesTester = tested.articles.test()
        tested.onEmptyViewButtonClicked()

        emptyViewTester.assertValues(false, true)
        articlesTester.assertValues(emptyList())
        emptyTextTester.assertValues("${R.string.empty_articles_sources_disabled}")
        emptyButtonTester.assertValues("${R.string.enable_sources}")
        viewActionsTester.assertValueCount(1)
        viewActionsTester.assertValueAt(0) { it == OpenSourcesListScreenCommand }
    }

    @Test
    fun `shows empty view with action if it has no articles but has active source`() {
        givenNoArticles()
        givenActiveSources()
        val emptyViewTester = tested.emptyViewVisible.test()
        val emptyTextTester = tested.emptyViewDescriptionText.test()
        val emptyButtonTester = tested.emptyViewButtonText.test()
        val viewActionsTester = tested.commands.test()

        val articlesTester = tested.articles.test()
        verify(articlesRepository, never()).refresh()
        tested.onEmptyViewButtonClicked()

        emptyViewTester.assertValues(false, true)
        articlesTester.assertValues(emptyList())
        emptyTextTester.assertValues("${R.string.empty_articles_must_refresh}")
        emptyButtonTester.assertValues("${R.string.refresh}")
        viewActionsTester.assertValueCount(0)
        verify(articlesRepository).refresh()
    }

    @Test
    fun `navigates to debug screen`() {
        val tester = tested.commands.test()

        tested.onDebugClicked()

        tester.assertValue { it is OpenDebugScreenCommand }
    }

    private fun givenActiveSources() {
        val source = Source(
            id = 1L,
            name = "asd",
            url = "asd",
            isActive = true
        )
        whenever(sourcesRepository.fetchSources()).thenReturn(Observable.just(listOf(source)))
    }

    private fun givenOpenArticleInAppSettingDisabled() {
        appSettings.providedOpenArticlesInApp = Observable.just(false)
    }

    private fun givenOpenArticleInAppSettingEnabled() {
        appSettings.providedOpenArticlesInApp = Observable.just(true)
    }

    private fun givenNoActiveSources() {
        val source = Source(
            id = 1L,
            name = "asd",
            url = "asd",
            isActive = false
        )
        whenever(sourcesRepository.fetchSources()).thenReturn(Observable.just(listOf(source)))
    }

    private fun givenNoSources() {
        whenever(sourcesRepository.fetchSources()).thenReturn(Observable.just(emptyList()))
    }

    private fun givenNoArticles() = givenHasArticles(emptyList())

    private fun givenHasArticles(articles: List<SourceArticle> = ARTICLES) {
        whenever(articlesRepository.fetchArticles()).thenReturn(Observable.just(articles))
    }

    private companion object {
        val ARTICLES = Array(3) { dummySourceArticle(it) }.toList()
    }
}