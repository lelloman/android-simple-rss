package com.lelloman.read

import android.support.test.rule.ActivityTestRule
import com.lelloman.read.http.HttpModule
import com.lelloman.read.testutils.MockHttpClient
import com.lelloman.read.testutils.MockHttpClient.Companion.URL_ASD
import com.lelloman.read.testutils.TestApp
import com.lelloman.read.testutils.rotateNatural
import com.lelloman.read.testutils.screen.AddSourceScreen
import com.lelloman.read.testutils.screen.ArticlesListScreen
import com.lelloman.read.testutils.screen.DiscoverSourcesScreen
import com.lelloman.read.testutils.screen.FoundFeedsScreen
import com.lelloman.read.testutils.screen.SettingsScreen
import com.lelloman.read.testutils.screen.SourcesListScreen
import com.lelloman.read.testutils.screen.WalkthroughScreen
import com.lelloman.read.ui.launcher.view.LauncherActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SmokeTest {

    @get:Rule
    val activityTestRule = ActivityTestRule<LauncherActivity>(LauncherActivity::class.java, true, false)

    @Before
    fun setUp() {
        rotateNatural()
        TestApp.instance.httpModule = object : HttpModule() {
            override fun provideOkHttpClient() = MockHttpClient()
        }
        TestApp.instance.appSettings.reset()
        TestApp.instance.db.clearAllTables()

        activityTestRule.launchActivity(null)
    }

    @Test
    fun smokeTest() {
        // from walkthrough to articles list
        WalkthroughScreen()
            .firstPageIsDisplayed()
            .clickOk()
            .themesAreDisplayed()
            .clickOnThemes()
            .swipeLeft()
            .firstPageIsDisplayed()
            .swipeRight()
            .swipeRight()
            .typeInUrlIsDisplayed()
            .swipeRight()
            .clickNo()

        // navigation from articles list screen
        ArticlesListScreen().clickOnSourcesInOverflow()
        SourcesListScreen().clickAddSource()
        AddSourceScreen().backToSourcesList()
        SourcesListScreen().backToArticlesList()
        ArticlesListScreen().clickOnSettingsInOverflow()
        SettingsScreen().backToArticlesList()
        ArticlesListScreen().clickOnDiscoverSourcesInOverflow()
        DiscoverSourcesScreen().typeUrl(URL_ASD)
            .closeKeyboard()
            .clickOnDiscover()
        FoundFeedsScreen()
            .wait(2)
            .displaysArticles(3)
            .hasText(URL_ASD)
            .backToDiscoverUrlScreen()
        DiscoverSourcesScreen()
            .hasText(URL_ASD)
            .backToArticlesList()
    }
}