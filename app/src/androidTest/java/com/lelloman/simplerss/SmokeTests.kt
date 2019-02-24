package com.lelloman.simplerss

import androidx.test.rule.ActivityTestRule
import com.lelloman.common.androidtestutils.closeKeyboard
import com.lelloman.common.androidtestutils.hasText
import com.lelloman.common.androidtestutils.wait
import com.lelloman.simplerss.testutils.MockHttpClient
import com.lelloman.simplerss.testutils.screen.ArticlesListScreen
import com.lelloman.simplerss.testutils.screen.WalkthroughScreen
import com.lelloman.simplerss.testutils.setUpTestAppWithMockedHttpStack
import com.lelloman.simplerss.ui.launcher.view.LauncherActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SmokeTests {

    @get:Rule
    val activityTestRule = ActivityTestRule(LauncherActivity::class.java, true, false)

    private var isNetworkMetered = false

    @Before
    fun setUp() {
        setUpTestAppWithMockedHttpStack { isNetworkMetered }
        activityTestRule.launchActivity(null)
    }

    @Test
    fun smokeTest1() {
        // from walkthrough to articles list
        WalkthroughScreen()
            .firstPageIsDisplayed()
            .clickNext()
            .secondPageIsDisplayed()
            .clickNext()
            .typeInUrlIsDisplayed()
            .swipeRight()
            .themesAreDisplayed()
            .clickOnThemes()
            .swipeLeft()
            .swipeLeft()
            .secondPageIsDisplayed()
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
            .typeUrl(MockHttpClient.URL_ASD)
            .closeKeyboard()
            .clickOnDiscover()
            .wait(2.0)
            .displaysFoundFeeds(3)
            .hasText(MockHttpClient.URL_ASD)
            .backToDiscoverUrlScreen()
            .hasText(MockHttpClient.URL_ASD)
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
            .showsArticles(MockHttpClient.FANPAGE_ARTICLES_COUNT + MockHttpClient.REPUBBLICA_ARTICLES_COUNT)

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
            .showsArticles(MockHttpClient.REPUBBLICA_ARTICLES_COUNT)

        // add another source and verify sources enable/disable
        ArticlesListScreen()
            .clickOnSourcesInOverflow()
            .clickAddSource()
            .typeSourceName("fanpage")
            .typeSourceUrl(MockHttpClient.URL_FANPAGE_FEED)
            .clickSave()
            .backToArticlesList()
            .showsArticles(MockHttpClient.REPUBBLICA_ARTICLES_COUNT + MockHttpClient.FANPAGE_ARTICLES_COUNT)
            .clickOnSourcesInOverflow()
            .clickOnSource("repubblica")
            .backToArticlesList()
            .showsArticles(MockHttpClient.FANPAGE_ARTICLES_COUNT)
            .clickOnSourcesInOverflow()
            .clickOnSource("repubblica")
            .clickOnSource("fanpage")
            .backToArticlesList()
            .showsArticles(MockHttpClient.REPUBBLICA_ARTICLES_COUNT)

        // clear data
        ArticlesListScreen()
            .clickOnSettingsInOverflow()
            .clickOnClearData()
            .clickOnOk()
            .backToArticlesList()
            .showsEmptyViewWithNoSourcesText()
    }
}