package com.lelloman.read

import android.support.test.rule.ActivityTestRule
import com.lelloman.read.core.TimeProvider
import com.lelloman.read.core.logger.LoggerFactory
import com.lelloman.read.http.HttpClient
import com.lelloman.read.http.HttpModule
import com.lelloman.read.http.HttpRequest
import com.lelloman.read.http.HttpResponse
import com.lelloman.read.screen.WalkthroughScreen
import com.lelloman.read.testutils.TestApp
import com.lelloman.read.testutils.rotateNatural
import com.lelloman.read.ui.launcher.view.LauncherActivity
import io.reactivex.Single
import okhttp3.OkHttpClient
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SmokeTest {

    @get:Rule
    val activityTestRule = ActivityTestRule<LauncherActivity>(LauncherActivity::class.java, true, false)

    private val httpClient: HttpClient = object : HttpClient {

        override fun request(request: HttpRequest): Single<HttpResponse> {
            return Single.error(Exception("MWAHAHA"))
        }
    }

    @Before
    fun setUp() {
        rotateNatural()
        TestApp.instance.httpModule = object : HttpModule() {
            override fun provideHttpClient(
                okHttpClient: OkHttpClient,
                loggerFactory: LoggerFactory,
                timeProvider: TimeProvider
            ) = httpClient
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
            // navigation from articles list
            // -sources
            .clickOnSourcesInOverflow()
            .clickAddSource()
            .backToSourcesList()
            .backToArticlesList()
            // -settings
            .clickOnSettingsInOverflow()
            .backToArticlesList()
            // -discover
            .clickOnDiscoverSourcesInOverflow()
            .typeUrl("www.asd.com")
            .closeKeyboard()
            .clickOnDiscover()
            .hasText("http://www.asd.com")
            .backToDiscoverUrlScreen()
            .hasText("http://www.asd.com")
            .backToArticlesList()
    }
}