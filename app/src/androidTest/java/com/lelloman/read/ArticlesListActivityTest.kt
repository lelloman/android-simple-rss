package com.lelloman.read

import android.arch.lifecycle.MutableLiveData
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.lelloman.read.articleslist.view.ArticlesListActivity
import com.lelloman.read.articleslist.viewmodel.ArticlesListViewModel
import com.lelloman.read.persistence.model.Article
import com.lelloman.read.testutils.TestApp
import com.lelloman.read.testutils.whenever
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ArticlesListActivityTest {

    @get:Rule
    val activityTestRule = ActivityTestRule<ArticlesListActivity>(ArticlesListActivity::class.java, true, false)

    private lateinit var viewModel: ArticlesListViewModel

    private lateinit var articlesLiveData: MutableLiveData<List<Article>>
    private lateinit var isLoadingLiveData: MutableLiveData<Boolean>

    @Before
    fun setUp() {
        articlesLiveData = MutableLiveData()
        isLoadingLiveData = MutableLiveData()

        viewModel = TestApp.instance.viewModelModule.articlesListViewModel
        whenever(viewModel.articles).thenReturn(articlesLiveData)
        whenever(viewModel.isLoading).thenReturn(isLoadingLiveData)
    }

    @Test
    fun opensList() {
        activityTestRule.launchActivity(null)

        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()))
        isLoadingLiveData.postValue(true)
        Thread.sleep(2000)
        isLoadingLiveData.postValue(false)
        Thread.sleep(2000)
    }
}