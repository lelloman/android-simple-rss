package com.lelloman.read

import android.net.Uri
import android.support.test.rule.ActivityTestRule
import android.widget.ImageView
import com.lelloman.read.core.MeteredConnectionChecker
import com.lelloman.read.core.PicassoWrap
import com.lelloman.read.core.di.AppModule
import com.lelloman.read.http.HttpModule
import com.lelloman.read.persistence.settings.AppSettings
import com.lelloman.read.testutils.MockHttpClient
import com.lelloman.read.testutils.MockHttpClient.Companion.FANPAGE_ARTICLES_COUNT
import com.lelloman.read.testutils.MockHttpClient.Companion.REPUBBLICA_ARTICLES_COUNT
import com.lelloman.read.testutils.MockHttpClient.Companion.URL_ASD
import com.lelloman.read.testutils.TestApp
import com.lelloman.read.testutils.rotateNatural
import com.lelloman.read.testutils.screen.ArticlesListScreen
import com.lelloman.read.testutils.screen.WalkthroughScreen
import com.lelloman.read.ui.launcher.view.LauncherActivity
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SmokeTest {

    @get:Rule
    val activityTestRule = ActivityTestRule<LauncherActivity>(LauncherActivity::class.java, true, false)

    @Before
    fun setUp() {
        rotateNatural()
        TestApp.dependenciesUpdate {
            it.appModule = object : AppModule(it) {
                override fun providePicassoWrap(appSettings: AppSettings, meteredConnectionChecker: MeteredConnectionChecker): PicassoWrap {
                    return object : PicassoWrap(appSettings, meteredConnectionChecker) {
                        override fun loadUrlIntoImageView(uri: Uri, view: ImageView, placeHolderId: Int?) {
                            Picasso
                                .get()
                                .load(uri)
                                .networkPolicy(NetworkPolicy.OFFLINE)
                                .placeholder(placeHolderId ?: 0)
                                .into(view)
                        }
                    }
                }
            }
            it.httpModule = object : HttpModule() {
                override fun provideOkHttpClient() = MockHttpClient()
            }
        }
        TestApp.resetPersistence()

        activityTestRule.launchActivity(null)
    }

    private fun fromWalkthroughToArticlesListFull() = WalkthroughScreen()
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

    private fun fromWalkthroughToArticlesListFast() = WalkthroughScreen()
        .pressClose()

    private fun navigationFromArticlesListScreen() = ArticlesListScreen()
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

    private fun add2Sources() = ArticlesListScreen()
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


    private fun openArticleInApp() = ArticlesListScreen()
        .clickOnSettingsInOverflow()
        .setOpenArticlesInApp(true)
        .backToArticlesList()
        .clickOnArticle(0)
        .backToArticlesList()

    private fun verifyArticlesImagesVisibility() = ArticlesListScreen()
        .showsArticles(FANPAGE_ARTICLES_COUNT + REPUBBLICA_ARTICLES_COUNT)
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

    @Test
    fun smokeTest() {
//        fromWalkthroughToArticlesListFast()
        fromWalkthroughToArticlesListFull()
        navigationFromArticlesListScreen()
        add2Sources()
        openArticleInApp()
        verifyArticlesImagesVisibility()
    }
}