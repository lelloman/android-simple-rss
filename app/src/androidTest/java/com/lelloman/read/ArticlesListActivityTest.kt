package com.lelloman.read

import android.arch.lifecycle.MutableLiveData
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.swipeDown
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.lelloman.read.articleslist.view.ArticlesListActivity
import com.lelloman.read.articleslist.viewmodel.ArticlesListViewModel
import com.lelloman.read.persistence.model.Article
import com.lelloman.read.testutils.TestApp
import com.lelloman.read.testutils.assertRecyclerViewCount
import com.lelloman.read.testutils.checkIsSwipeRefreshing
import com.lelloman.read.testutils.onUiThread
import com.lelloman.read.testutils.viewIsDisplayed
import com.lelloman.read.testutils.wait
import com.lelloman.read.testutils.whenever
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.never
import org.mockito.Mockito.verify

@RunWith(AndroidJUnit4::class)
class ArticlesListActivityTest {

    @get:Rule
    val activityTestRule = ActivityTestRule<ArticlesListActivity>(ArticlesListActivity::class.java, true, false)

    private lateinit var viewModel: ArticlesListViewModel

    private lateinit var articlesLiveData: MutableLiveData<List<Article>>
    private lateinit var isLoadingLiveData: MutableLiveData<Boolean>

    private val articles = Array(20, {
        Article(
            it.toLong(),
            "Article $it",
            "Subtitle $it",
            it.toLong(),
            "Source",
            0L
        )
    }).toList()

    @Before
    fun setUp() {
        articlesLiveData = MutableLiveData()
        isLoadingLiveData = MutableLiveData()

        viewModel = TestApp.instance.viewModelModule.articlesListViewModel
        whenever(viewModel.articles).thenReturn(articlesLiveData)
        whenever(viewModel.isLoading).thenReturn(isLoadingLiveData)

        activityTestRule.launchActivity(null)
    }

    @Test
    fun showsSwipeRefreshWhenLoading() {
        viewIsDisplayed(R.id.recycler_view)
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
    fun showsArticles(){
        assertRecyclerViewCount(0)

        onUiThread { articlesLiveData.value = articles }
        wait(0.2)

        assertRecyclerViewCount(articles.size)
    }
}