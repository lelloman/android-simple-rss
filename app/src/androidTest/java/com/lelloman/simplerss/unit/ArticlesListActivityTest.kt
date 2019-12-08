package com.lelloman.simplerss.unit

import androidx.lifecycle.MutableLiveData
import androidx.test.rule.ActivityTestRule
import com.lelloman.common.androidtestutils.onUiThread
import com.lelloman.common.androidtestutils.rotateLeft
import com.lelloman.common.androidtestutils.rotateNatural
import com.lelloman.common.androidtestutils.rotateRight
import com.lelloman.common.androidtestutils.wait
import com.lelloman.common.androidtestutils.whenever
import com.lelloman.common.view.AppTheme
import com.lelloman.common.view.actionevent.ViewActionEvent
import com.lelloman.simplerss.di.MockViewModelModuleFactory
import com.lelloman.simplerss.persistence.db.model.SourceArticle
import com.lelloman.simplerss.testutils.TestApp
import com.lelloman.simplerss.testutils.screen.ArticlesListScreen
import com.lelloman.simplerss.ui.articles.view.ArticlesListActivity
import com.lelloman.simplerss.ui.articles.viewmodel.ArticlesListViewModel
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import java.util.*

class ArticlesListActivityTest {

    @get:Rule
    val activityTestRule = ActivityTestRule<ArticlesListActivity>(ArticlesListActivity::class.java, true, false)

    private val viewModelModule = MockViewModelModuleFactory()
    private lateinit var viewModel: ArticlesListViewModel

    private lateinit var articlesLiveData: MutableLiveData<List<SourceArticle>>
    private lateinit var isLoadingLiveData: MutableLiveData<Boolean>
    private lateinit var viewActionEvents: Subject<ViewActionEvent>
    private lateinit var themeChangedEvents: Subject<AppTheme>

    private lateinit var screen: ArticlesListScreen

    private val articles = Array(20) {
        SourceArticle(
            id = it.toLong(),
            title = "Article $it",
            subtitle = "Subtitle $it",
            content = "",
            link = "www.staceppa.com/$it",
            time = it.toLong(),
            sourceName = "Source",
            sourceId = 0L,
            imageUrl = "",
            favicon = null
        )
    }.toList()

    @Before
    fun setUp() {
        rotateNatural()
        articlesLiveData = MutableLiveData()
        isLoadingLiveData = MutableLiveData()
        viewActionEvents = PublishSubject.create()
        themeChangedEvents = PublishSubject.create()

        TestApp.resetPersistence()
        TestApp.dependenciesUpdate { it.viewModelModuleFactory = viewModelModule }
        viewModel = viewModelModule.articlesListViewModel
        whenever(viewModel.articles).thenReturn(articlesLiveData)
        whenever(viewModel.isLoading).thenReturn(isLoadingLiveData)
        whenever(viewModel.viewActionEvents).thenReturn(viewActionEvents)
        whenever(viewModel.themeChangedEvents).thenReturn(themeChangedEvents)

        activityTestRule.launchActivity(null)
        screen = ArticlesListScreen()
    }

    @After
    fun tearDown() {
        rotateNatural()
    }

    @Test
    fun showsSwipeRefreshWhenLoading() {
        screen.isNotSwipeRefreshing()

        onUiThread { isLoadingLiveData.value = true }
        wait(0.1)
        screen.isSwipeRefreshing()

        onUiThread { isLoadingLiveData.value = false }
        wait(0.1)
        screen.isNotSwipeRefreshing()
    }

    @Test
    fun triggersRefreshWhenSwipeDown() {
        verify(viewModel, never()).refresh()

        screen.swipeToRefresh()

        verify(viewModel).refresh()
    }

    @Test
    fun showsArticles() {
        screen.showsArticles(0)

        onUiThread { articlesLiveData.value = articles }
        wait(0.2)

        screen.showsArticles(articles.size)
    }

    @Test
    fun retainsLoadingStateOnRotation() {
        rotateNatural()
        isLoadingLiveData.postValue(true)
        wait(0.2)

        screen
            .isSwipeRefreshing()
            .rotateLeft()
            .isSwipeRefreshing()
            .rotateRight()
            .isSwipeRefreshing()

        isLoadingLiveData.postValue(false)
        wait(0.2)

        screen
            .isNotSwipeRefreshing()
            .rotateNatural()
            .isNotSwipeRefreshing()
    }

    @Test
    fun retainsArticleOnRotation() {
        val random = Random()
        val article = SourceArticle(
            id = random.nextLong(),
            title = random.nextLong().toString(),
            subtitle = random.nextLong().toString(),
            content = random.nextLong().toString(),
            link = random.nextLong().toString(),
            time = random.nextLong(),
            sourceName = random.nextLong().toString(),
            favicon = null,
            sourceId = random.nextLong(),
            imageUrl = random.nextLong().toString()
        )
        articlesLiveData.postValue(listOf(article))
        wait(0.2)

        screen
            .showsArticles(1)
            .showsArticleWithTitle(0, article.title)
            .rotateLeft()
            .showsArticles(1)
            .showsArticleWithTitle(0, article.title)
            .rotateRight()
            .showsArticles(1)
            .showsArticleWithTitle(0, article.title)
    }
}