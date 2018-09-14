package com.lelloman.read.unit

import android.arch.lifecycle.MutableLiveData
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.lelloman.common.utils.SingleLiveData
import com.lelloman.read.core.view.actionevent.ViewActionEvent
import com.lelloman.read.di.MockViewModelModule
import com.lelloman.read.persistence.db.model.SourceArticle
import com.lelloman.read.testutils.TestApp
import com.lelloman.read.testutils.onUiThread
import com.lelloman.read.testutils.rotateNatural
import com.lelloman.read.testutils.screen.ArticlesListScreen
import com.lelloman.read.testutils.wait
import com.lelloman.read.testutils.whenever
import com.lelloman.read.ui.articles.view.ArticlesListActivity
import com.lelloman.read.ui.articles.viewmodel.ArticlesListViewModel
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import java.util.*

@RunWith(AndroidJUnit4::class)
class ArticlesListActivityTest {

    @get:Rule
    val activityTestRule = ActivityTestRule<ArticlesListActivity>(ArticlesListActivity::class.java, true, false)

    private val viewModelModule = MockViewModelModule()
    private lateinit var viewModel: ArticlesListViewModel

    private lateinit var articlesLiveData: MutableLiveData<List<SourceArticle>>
    private lateinit var isLoadingLiveData: MutableLiveData<Boolean>
    private lateinit var viewActionEvents: SingleLiveData<ViewActionEvent>

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
        viewActionEvents = SingleLiveData()

        TestApp.dependenciesUpdate { it.viewModelModule = viewModelModule }
        viewModel = viewModelModule.articlesListViewModel
        whenever(viewModel.articles).thenReturn(articlesLiveData)
        whenever(viewModel.isLoading).thenReturn(isLoadingLiveData)
        whenever(viewModel.viewActionEvents).thenReturn(viewActionEvents)

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