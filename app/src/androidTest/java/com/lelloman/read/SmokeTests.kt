package com.lelloman.read

import android.content.Context
import android.support.test.rule.ActivityTestRule
import com.lelloman.common.view.MeteredConnectionChecker
import com.lelloman.read.core.di.AppModule
import com.lelloman.read.http.HttpModule
import com.lelloman.read.testutils.MockHttpClient
import com.lelloman.read.testutils.MockHttpClient.Companion.FANPAGE_ARTICLES_COUNT
import com.lelloman.read.testutils.MockHttpClient.Companion.REPUBBLICA_ARTICLES_COUNT
import com.lelloman.read.testutils.MockHttpClient.Companion.URL_ASD
import com.lelloman.read.testutils.TestApp
import com.lelloman.read.testutils.rotateNatural
import com.lelloman.read.testutils.screen.ArticlesListScreen
import com.lelloman.read.testutils.screen.WalkthroughScreen
import com.lelloman.read.ui.launcher.view.LauncherActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SmokeTests {

    @get:Rule
    val activityTestRule = ActivityTestRule<LauncherActivity>(LauncherActivity::class.java, true, false)

    private var isNetworkMetered = false

    @Before
    fun setUp() {
        rotateNatural()
        TestApp.dependenciesUpdate {
            it.httpModule = object : HttpModule() {
                override fun provideOkHttpClient() = MockHttpClient()
            }
            it.appModule = object : AppModule(it) {
                override fun provideMeteredConnectionChecker(context: Context): MeteredConnectionChecker {
                    return object : MeteredConnectionChecker {
                        override fun isNetworkMetered() = isNetworkMetered
                    }
                }
            }
        }
        TestApp.resetPersistence()

        activityTestRule.launchActivity(null)
    }

    @Test
    fun smokeTest1() {
        // from walkthrough to articles list
        WalkthroughScreen()
            .firstPageIsDisplayed()
            .clickOk()
            .typeInUrlIsDisplayed()
            .swipeRight()
            .themesAreDisplayed()
            .clickOnThemes()
            .swipeLeft()
            .swipeLeft()
            .firstPageIsDisplayed()
            .swipeRight()
            .swipeRight()
            .swipeRight()
            .clickNo()

        // navigation from articles list screen
        ArticlesListScreen()
            .clickOnSourcesInOverflow()
            .clickAddSource()
            .backToSourcesList()
            .backToArticlesList()
            .clickOnSettingsInOverflow()
            .backToArticlesList()
            .clickOnDiscoverSourcesInOverflow()
            .typeUrl(URL_ASD)
            .closeKeyboard()
            .clickOnDiscover()
            .wait(1)
            .displaysFoundFeeds(3)
            .hasText(URL_ASD)
            .backToDiscoverUrlScreen()
            .hasText(URL_ASD)
            .backToArticlesList()

        // add 2 sources
        ArticlesListScreen()
            .clickOnSourcesInOverflow()
            .clickAddSource()
            .typeSourceName("repubblica")
            .typeSourceUrl(MockHttpClient.URL_REPUBBLICA_FEED)
            .urlFieldHasNoDrawable()
            .clickTestUrl()
            .wait(1.0)
            .urlFieldShowsOkDrawable()
            .clickSave()
            .clickAddSource()
            .typeSourceName("fanpage")
            .typeSourceUrl(MockHttpClient.URL_FANPAGE_FEED)
            .clickTestUrl()
            .wait(1.0)
            .urlFieldShowsOkDrawable()
            .clickSave()
            .showsSources(2)
            .backToArticlesList()
            .showsArticles(FANPAGE_ARTICLES_COUNT + REPUBBLICA_ARTICLES_COUNT)

        // verify articles images visibility
        ArticlesListScreen()
            .articleWithImageAt(0)
            .articleWithoutImageAt(1)
            .articleWithImageAt(2)
            .articleWithoutImageAt(3)
            .articleWithoutImageAt(4)
            .articleWithImageAt(5)
            .clickOnSettingsInOverflow()
            .setArticlesImages(false)
            .backToArticlesList()
            .articleWithoutImageAt(0)
            .articleWithoutImageAt(1)
            .articleWithoutImageAt(2)
            .articleWithoutImageAt(3)
            .articleWithoutImageAt(4)
            .articleWithoutImageAt(5)

        // open article in app
        ArticlesListScreen()
            .clickOnSettingsInOverflow()
            .setOpenArticlesInApp(true)
            .backToArticlesList()
            .clickOnArticle(0)
            .backToArticlesList()
    }

    @Test
    fun smokeTest2() {
        WalkthroughScreen().pressClose()

        // add source and verify use metered network option
        ArticlesListScreen()
            .clickOnSettingsInOverflow()
            .setUseMeteredNetwork(false)
            .backToArticlesList()
            .showsEmptyViewWithNoSourcesText()
            .apply { isNetworkMetered = true }
            .clickOnSourcesInOverflow()
            .clickAddSource()
            .typeSourceName("repubblica")
            .typeSourceUrl(MockHttpClient.URL_REPUBBLICA_FEED)
            .clickSave()
            .backToArticlesList()
            .showsArticles(0)
            .showsEmptyViewWithNoArticlesText()
            .apply { isNetworkMetered = false }
            .swipeToRefresh()
            .wait(2.0)
            .showsArticles(REPUBBLICA_ARTICLES_COUNT)

        // add another source and verify sources enable/disable
        ArticlesListScreen()
            .clickOnSourcesInOverflow()
            .clickAddSource()
            .typeSourceName("fanpage")
            .typeSourceUrl(MockHttpClient.URL_FANPAGE_FEED)
            .clickSave()
            .backToArticlesList()
            .showsArticles(REPUBBLICA_ARTICLES_COUNT + FANPAGE_ARTICLES_COUNT)
            .clickOnSourcesInOverflow()
            .clickOnSource("repubblica")
            .backToArticlesList()
            .showsArticles(FANPAGE_ARTICLES_COUNT)
            .clickOnSourcesInOverflow()
            .clickOnSource("repubblica")
            .clickOnSource("fanpage")
            .backToArticlesList()
            .showsArticles(REPUBBLICA_ARTICLES_COUNT)
    }
}