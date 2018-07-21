package com.lelloman.read.ui.articles.viewmodel

import com.google.common.truth.Truth.assertThat
import com.lelloman.read.R
import com.lelloman.read.core.navigation.NavigationScreen
import com.lelloman.read.core.navigation.ScreenNavigationEvent
import com.lelloman.read.core.navigation.ViewIntentNavigationEvent
import com.lelloman.read.persistence.db.model.Source
import com.lelloman.read.persistence.db.model.SourceArticle
import com.lelloman.read.persistence.settings.AppSettings
import com.lelloman.read.testutils.AndroidArchTest
import com.lelloman.read.testutils.MockResourceProvider
import com.lelloman.read.testutils.dummySourceArticle
import com.lelloman.read.testutils.test
import com.lelloman.read.ui.articles.repository.ArticlesRepository
import com.lelloman.read.ui.sources.repository.SourcesRepository
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers.trampoline
import io.reactivex.subjects.BehaviorSubject
import org.junit.Test

class ArticlesListViewModelImplTest : AndroidArchTest() {

    private val articlesRepository: ArticlesRepository = mock()
    private val sourcesRepository: SourcesRepository = mock()
    private val resourceProvider = MockResourceProvider()
    private val appSettings: AppSettings = mock()

    private lateinit var tested: ArticlesListViewModelImpl

    override fun setUp() {
        tested = ArticlesListViewModelImpl(
            ioScheduler = trampoline(),
            uiScheduler = trampoline(),
            articlesRepository = articlesRepository,
            sourcesRepository = sourcesRepository,
            resourceProvider = resourceProvider,
            appSettings = appSettings
        )
    }

    @Test
    fun `triggers articles refresh`() {
        tested.refresh()

        verify(articlesRepository).refresh()
    }

    @Test
    fun `navigates to sources list when click on sources button`() {
        val viewActions = tested.viewActionEvents.test()

        tested.onSourcesClicked()

        assertThat(viewActions.values).hasSize(1)
        viewActions.values[0].apply {
            assertThat(this).isInstanceOf(ScreenNavigationEvent::class.java)
            (this as ScreenNavigationEvent).apply {
                assertThat(args).isEmpty()
                assertThat(targetClass).isEqualTo(NavigationScreen.SOURCES_LIST)
            }
        }
    }

    @Test
    fun `navigates to article screen when article is clicked and open in app setting is true`() {
        givenOpenArticleInAppSettingEnabled()
        val viewActions = tested.viewActionEvents.test()
        val article: SourceArticle = mock()

        tested.onArticleClicked(article)

        assertThat(viewActions.values).hasSize(1)
        viewActions.values[0].apply {
            assertThat(this).isInstanceOf(ScreenNavigationEvent::class.java)
            (this as ScreenNavigationEvent).apply {
                assertThat(args).hasLength(1)
                assertThat(args[0]).isEqualTo(article)
                assertThat(targetClass).isEqualTo(NavigationScreen.ARTICLE)
            }
        }
    }

    @Test
    fun `navigates to view intent when article is clicked and open in app setting is false`() {
        val link = "asdasd"
        givenOpenArticleInAppSettingDisabled()
        val viewActions = tested.viewActionEvents.test()
        val article = dummySourceArticle().copy(link = link)

        tested.onArticleClicked(article)
        viewActions.assertValues(ViewIntentNavigationEvent(link))
    }

    @Test
    fun `navigates to settings screen when settings button is clicked`() {
        val viewActions = tested.viewActionEvents.test()

        tested.onSettingsClicked()

        assertThat(viewActions.values).hasSize(1)
        viewActions.values[0].apply {
            assertThat(this).isInstanceOf(ScreenNavigationEvent::class.java)
            (this as ScreenNavigationEvent).apply {
                assertThat(args).isEmpty()
                assertThat(targetClass).isEqualTo(NavigationScreen.SETTINGS)
            }
        }
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
        val viewActionsTester = tested.viewActionEvents.test()

        val articlesTester = tested.articles.test()
        tested.onEmptyViewButtonClicked()

        emptyViewTester.assertValues(false, true)
        articlesTester.assertValues(emptyList())
        emptyTextTester.assertValues("${R.string.empty_articles_no_source}")
        emptyButtonTester.assertValues("${R.string.add_source}")
        viewActionsTester.assertValuesCount(1)
        viewActionsTester.values[0].apply {
            assertThat(this).isInstanceOf(ScreenNavigationEvent::class.java)
            (this as ScreenNavigationEvent).apply {
                assertThat(args).isEmpty()
                assertThat(targetClass).isEqualTo(NavigationScreen.ADD_SOURCE)
            }
        }
    }

    @Test
    fun `shows empty view with action if it has no articles and no active source`() {
        givenNoArticles()
        givenNoActiveSources()
        val emptyViewTester = tested.emptyViewVisible.test()
        val emptyTextTester = tested.emptyViewDescriptionText.test()
        val emptyButtonTester = tested.emptyViewButtonText.test()
        val viewActionsTester = tested.viewActionEvents.test()

        val articlesTester = tested.articles.test()
        tested.onEmptyViewButtonClicked()

        emptyViewTester.assertValues(false, true)
        articlesTester.assertValues(emptyList())
        emptyTextTester.assertValues("${R.string.empty_articles_sources_disabled}")
        emptyButtonTester.assertValues("${R.string.enable_sources}")
        viewActionsTester.assertValuesCount(1)
        viewActionsTester.values[0].apply {
            assertThat(this).isInstanceOf(ScreenNavigationEvent::class.java)
            (this as ScreenNavigationEvent).apply {
                assertThat(args).isEmpty()
                assertThat(targetClass).isEqualTo(NavigationScreen.SOURCES_LIST)
            }
        }
    }

    @Test
    fun `shows empty view with action if it has no articles but has active source`() {
        givenNoArticles()
        givenActiveSources()
        val emptyViewTester = tested.emptyViewVisible.test()
        val emptyTextTester = tested.emptyViewDescriptionText.test()
        val emptyButtonTester = tested.emptyViewButtonText.test()
        val viewActionsTester = tested.viewActionEvents.test()

        val articlesTester = tested.articles.test()
        verify(articlesRepository, never()).refresh()
        tested.onEmptyViewButtonClicked()

        emptyViewTester.assertValues(false, true)
        articlesTester.assertValues(emptyList())
        emptyTextTester.assertValues("${R.string.empty_articles_must_refresh}")
        emptyButtonTester.assertValues("${R.string.refresh}")
        viewActionsTester.assertValuesCount(0)
        verify(articlesRepository).refresh()
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
        whenever(appSettings.openArticlesInApp).thenReturn(Observable.just(false))
    }

    private fun givenOpenArticleInAppSettingEnabled() {
        whenever(appSettings.openArticlesInApp).thenReturn(Observable.just(true))
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