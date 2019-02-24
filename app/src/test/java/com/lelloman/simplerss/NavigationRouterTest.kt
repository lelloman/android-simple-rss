package com.lelloman.simplerss

import android.app.Activity
import com.lelloman.common.navigation.DeepLink
import com.lelloman.common.navigation.DeepLinkNavigationEvent
import com.lelloman.common.navigation.DeepLinkStartable
import com.lelloman.common.navigation.NavigationRouter
import com.lelloman.simplerss.navigation.SimpleRssNavigationScreen
import com.lelloman.simplerss.navigation.SimpleRssNavigationScreen.Companion.ARG_URL
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.junit.Test

class NavigationRouterTest {

    private val applicationPackageName = "cra cra cra"
    private val tested = NavigationRouter(
        packageManager = mock(),
        applicationPackageName = applicationPackageName
    )

    private val starter: DeepLinkStartable = mock()

    @Test
    fun `finds starter methods for SOURCES_LIST`() {
        val deepLink = DeepLink(SimpleRssNavigationScreen.SOURCES_LIST)
        SimpleRssNavigationScreen.SOURCES_LIST.deepLinkStartable = starter

        tested.handleDeepLink(NavigationRouterTest.ACTIVITY, DeepLinkNavigationEvent(deepLink))

        verify(starter).start(NavigationRouterTest.Companion.ACTIVITY, deepLink)
    }

    @Test
    fun `finds starter methods for ADD_SOURCE`() {
        val deepLink = DeepLink(SimpleRssNavigationScreen.ADD_SOURCE)
        SimpleRssNavigationScreen.ADD_SOURCE.deepLinkStartable = starter

        tested.handleDeepLink(NavigationRouterTest.Companion.ACTIVITY, DeepLinkNavigationEvent(deepLink))

        verify(starter).start(NavigationRouterTest.Companion.ACTIVITY, deepLink)
    }

    @Test
    fun `finds starter methods for ARTICLE`() {
        val url = "www.meow.com"
        val deepLink = DeepLink(SimpleRssNavigationScreen.ARTICLE).putString(ARG_URL, url)
        SimpleRssNavigationScreen.ARTICLE.deepLinkStartable = starter

        tested.handleDeepLink(NavigationRouterTest.Companion.ACTIVITY, DeepLinkNavigationEvent(deepLink))

        verify(starter).start(NavigationRouterTest.Companion.ACTIVITY, deepLink)
    }

    @Test
    fun `finds starter methods for SETTINGS`() {
        val deepLink = DeepLink(SimpleRssNavigationScreen.SETTINGS)
        SimpleRssNavigationScreen.SETTINGS.deepLinkStartable = starter

        tested.handleDeepLink(NavigationRouterTest.Companion.ACTIVITY, DeepLinkNavigationEvent(deepLink))

        verify(starter).start(NavigationRouterTest.Companion.ACTIVITY, deepLink)
    }

    @Test
    fun `finds starter methods for WALKTHROUGH`() {
        val deepLink = DeepLink(SimpleRssNavigationScreen.WALKTHROUGH)
        SimpleRssNavigationScreen.WALKTHROUGH.deepLinkStartable = starter

        tested.handleDeepLink(NavigationRouterTest.Companion.ACTIVITY, DeepLinkNavigationEvent(deepLink))

        verify(starter).start(NavigationRouterTest.Companion.ACTIVITY, deepLink)
    }

    @Test
    fun `finds starter methods for ARTICLES_LIST`() {
        val deepLink = DeepLink(SimpleRssNavigationScreen.ARTICLES_LIST)
        SimpleRssNavigationScreen.ARTICLES_LIST.deepLinkStartable = starter

        tested.handleDeepLink(NavigationRouterTest.Companion.ACTIVITY, DeepLinkNavigationEvent(deepLink))

        verify(starter).start(NavigationRouterTest.Companion.ACTIVITY, deepLink)
    }

    @Test
    fun `finds starter methods for DISCOVER_URL`() {
        val deepLink = DeepLink(SimpleRssNavigationScreen.DISCOVER_URL)
        SimpleRssNavigationScreen.DISCOVER_URL.deepLinkStartable = starter

        tested.handleDeepLink(NavigationRouterTest.Companion.ACTIVITY, DeepLinkNavigationEvent(deepLink))

        verify(starter).start(NavigationRouterTest.Companion.ACTIVITY, deepLink)
    }

    @Test
    fun `finds starter methods for FOUND_FEED_LIST`() {
        val url = "www.meow.com"
        val deepLink = DeepLink(SimpleRssNavigationScreen.FOUND_FEED_LIST).putString(ARG_URL, url)
        SimpleRssNavigationScreen.FOUND_FEED_LIST.deepLinkStartable = starter

        tested.handleDeepLink(NavigationRouterTest.Companion.ACTIVITY, DeepLinkNavigationEvent(deepLink))

        verify(starter).start(NavigationRouterTest.Companion.ACTIVITY, deepLink)
    }

    @Test
    fun `finds starter method for CLEAR_DATA_CONFIRMATION`() {
        val deepLink = DeepLink(SimpleRssNavigationScreen.CLEAR_DATA_CONFIRMATION)
        SimpleRssNavigationScreen.CLEAR_DATA_CONFIRMATION.deepLinkStartable = starter

        tested.handleDeepLink(NavigationRouterTest.ACTIVITY, DeepLinkNavigationEvent(deepLink))

        verify(starter).start(NavigationRouterTest.ACTIVITY, deepLink)
    }

    private companion object {
        val ACTIVITY: Activity = object : Activity() {}
    }
}