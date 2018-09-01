package com.lelloman.read.unit

import android.arch.lifecycle.MutableLiveData
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.swipeDown
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.lelloman.read.R
import com.lelloman.read.core.view.actionevent.ViewActionEvent
import com.lelloman.read.di.MockViewModelModule
import com.lelloman.read.persistence.db.model.SourceArticle
import com.lelloman.read.testutils.TestApp
import com.lelloman.read.testutils.checkIsSwipeRefreshing
import com.lelloman.read.testutils.checkRecyclerViewCount
import com.lelloman.read.testutils.checkViewAtPositionHasText
import com.lelloman.read.testutils.onUiThread
import com.lelloman.read.testutils.rotateLeft
import com.lelloman.read.testutils.rotateNatural
import com.lelloman.read.testutils.rotateRight
import com.lelloman.read.testutils.viewIsDisplayed
import com.lelloman.read.testutils.wait
import com.lelloman.read.testutils.whenever
import com.lelloman.read.ui.articles.view.ArticlesListActivity
import com.lelloman.read.ui.articles.viewmodel.ArticlesListViewModel
import com.lelloman.read.utils.SingleLiveData
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

        TestApp.instance.viewModelModule = viewModelModule
        viewModel = viewModelModule.articlesListViewModel
        whenever(viewModel.articles).thenReturn(articlesLiveData)
        whenever(viewModel.isLoading).thenReturn(isLoadingLiveData)
        whenever(viewModel.viewActionEvents).thenReturn(viewActionEvents)

        activityTestRule.launchActivity(null)
    }

    @After
    fun tearDown() {
        rotateNatural()
    }

    @Test
    fun showsSwipeRefreshWhenLoading() {
        viewIsDisplayed(R.id.articles_recycler_view)
        checkIsSwipeRefreshing(false)

        onUiThread { isLoadingLiveData.value = true }
        wait(0.1)
        checkIsSwipeRefreshing(true)

        onUiThread { isLoadingLiveData.value = false }
        wait(0.1)
        checkIsSwipeRefreshing(false)
    }

    @Test
    fun triggersRefreshWhenSwipeDown() {
        verify(viewModel, never()).refresh()

        onView(withId(R.id.swipe_refresh_layout)).perform(swipeDown())

        verify(viewModel).refresh()
    }

    @Test
    fun showsArticles() {
        checkRecyclerViewCount(0, R.id.articles_recycler_view)

        onUiThread { articlesLiveData.value = articles }
        wait(0.2)

        checkRecyclerViewCount(articles.size, R.id.articles_recycler_view)
    }

    @Test
    fun retainsLoadingStateOnRotation() {
        rotateNatural()
        isLoadingLiveData.postValue(true)
        wait(0.2)

        checkIsSwipeRefreshing(true)
        rotateLeft()
        checkIsSwipeRefreshing(true)

        rotateRight()
        checkIsSwipeRefreshing(true)
        isLoadingLiveData.postValue(false)
        wait(0.2)
        checkIsSwipeRefreshing(false)

        rotateNatural()
        checkIsSwipeRefreshing(false)
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

        checkRecyclerViewCount(1, R.id.articles_recycler_view)
        checkViewAtPositionHasText(0, article.title, R.id.articles_recycler_view)

        rotateLeft()
        checkRecyclerViewCount(1, R.id.articles_recycler_view)
        checkViewAtPositionHasText(0, article.title, R.id.articles_recycler_view)

        rotateRight()
        checkRecyclerViewCount(1, R.id.articles_recycler_view)
        checkViewAtPositionHasText(0, article.title, R.id.articles_recycler_view)
    }


}