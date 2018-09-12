package com.lelloman.read.core.navigation

import android.app.Activity
import com.google.common.truth.Truth.assertThat
import com.lelloman.read.mock.MockLoggerFactory
import com.lelloman.read.persistence.db.model.SourceArticle
import com.lelloman.read.ui.articles.view.ArticleActivity
import com.lelloman.read.ui.settings.view.SettingsActivity
import com.lelloman.read.ui.sources.view.AddSourceActivity
import com.lelloman.read.ui.sources.view.SourceActivity
import com.lelloman.read.ui.sources.view.SourcesListActivity
import com.lelloman.read.ui.walkthrough.view.WalkthroughActivity
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.junit.Test

class NavigationRouterTest {

    private val tested = NavigationRouter(MockLoggerFactory())

    @Test
    fun `finds starter methods for SOURCES_LIST`() {
        val starterMethod = tested.findStarterMethod(NavigationScreen.SOURCES_LIST, emptyArray()).toString()

        assertThat(starterMethod).isEqualTo(SourcesListActivity.Companion::start.toString())
    }

    @Test
    fun `finds starter methods for ADD_SOURCE with no args`() {
        val starterMethod = tested.findStarterMethod(NavigationScreen.ADD_SOURCE, emptyArray()).toString()

        assertThat(starterMethod).isEqualTo(AddSourceActivity.Companion::start.toString())
    }

    @Test
    fun `finds starter methods for ADD_SOURCE with prefill`() {
        val starterMethod = tested.findStarterMethod(NavigationScreen.ADD_SOURCE, arrayOf("name", "url")).toString()

        assertThat(starterMethod).isEqualTo(AddSourceActivity.Companion::startWithPrefill.toString())
    }

    @Test
    fun `finds starter methods for SOURCE`() {
        val id: Long? = 1L
        val starterMethod = tested.findStarterMethod(NavigationScreen.SOURCE, arrayOf(id!!)).toString()

        assertThat(starterMethod).isEqualTo(SourceActivity.Companion::start.toString())
    }

    @Test
    fun `finds starter methods for ARTICLE`() {
        val starterMethod = tested.findStarterMethod(NavigationScreen.ARTICLE, arrayOf(mock<SourceArticle>())).toString()

        assertThat(starterMethod).isEqualTo(ArticleActivity.Companion::start.toString())
    }

    @Test
    fun `finds starter methods for SETTINGS`() {
        val starterMethod = tested.findStarterMethod(NavigationScreen.SETTINGS, emptyArray()).toString()

        assertThat(starterMethod).isEqualTo(SettingsActivity.Companion::start.toString())
    }

    @Test
    fun `finds starter methods for WALKTHROUGH`() {
        val starterMethod = tested.findStarterMethod(NavigationScreen.WALKTHROUGH, emptyArray()).toString()

        assertThat(starterMethod).isEqualTo(WalkthroughActivity.Companion::start.toString())
    }

    @Test
    fun `finds starter methods for ARTICLES_LIST`() {
        val deepLink = DeepLink(NavigationScreen.ARTICLES_LIST)
        val starter: DeepLinkStartable = mock()
        NavigationScreen.ARTICLES_LIST.deepLinkStartable = starter

        tested.handleDeepLink(ACTIVITY, DeepLinkNavigationEvent(deepLink))

        verify(starter).start(ACTIVITY, deepLink)
    }

    private companion object {
        val ACTIVITY: Activity = object : Activity() {}
    }
}