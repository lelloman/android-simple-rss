package com.lelloman.simplerss

import android.app.Activity
import com.lelloman.common.navigation.DeepLink
import com.lelloman.common.navigation.DeepLinkNavigationEvent
import com.lelloman.common.navigation.DeepLinkStartable
import com.lelloman.common.navigation.NavigationRouter
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
        val deepLink = DeepLink(com.lelloman.simplerss.navigation.SimpleRssNavigationScreen.SOURCES_LIST)
        com.lelloman.simplerss.navigation.SimpleRssNavigationScreen.SOURCES_LIST.deepLinkStartable = starter

        tested.handleDeepLink(com.lelloman.simplerss.NavigationRouterTest.Companion.ACTIVITY, DeepLinkNavigationEvent(deepLink))

        verify(starter).start(com.lelloman.simplerss.NavigationRouterTest.Companion.ACTIVITY, deepLink)
    }

    @Test
    fun `finds starter methods for ADD_SOURCE`() {
        val deepLink = DeepLink(com.lelloman.simplerss.navigation.SimpleRssNavigationScreen.ADD_SOURCE)
        com.lelloman.simplerss.navigation.SimpleRssNavigationScreen.ADD_SOURCE.deepLinkStartable = starter

        tested.handleDeepLink(com.lelloman.simplerss.NavigationRouterTest.Companion.ACTIVITY, DeepLinkNavigationEvent(deepLink))

        verify(starter).start(com.lelloman.simplerss.NavigationRouterTest.Companion.ACTIVITY, deepLink)
    }

    @Test
    fun `finds starter methods for ARTICLE`() {
        val url = "www.meow.com"
        val deepLink = DeepLink(com.lelloman.simplerss.navigation.SimpleRssNavigationScreen.ARTICLE).putString(ARG_URL, url)
        com.lelloman.simplerss.navigation.SimpleRssNavigationScreen.ARTICLE.deepLinkStartable = starter

        tested.handleDeepLink(com.lelloman.simplerss.NavigationRouterTest.Companion.ACTIVITY, DeepLinkNavigationEvent(deepLink))

        verify(starter).start(com.lelloman.simplerss.NavigationRouterTest.Companion.ACTIVITY, deepLink)
    }

    @Test
    fun `finds starter methods for SETTINGS`() {
        val deepLink = DeepLink(com.lelloman.simplerss.navigation.SimpleRssNavigationScreen.SETTINGS)
        com.lelloman.simplerss.navigation.SimpleRssNavigationScreen.SETTINGS.deepLinkStartable = starter

        tested.handleDeepLink(com.lelloman.simplerss.NavigationRouterTest.Companion.ACTIVITY, DeepLinkNavigationEvent(deepLink))

        verify(starter).start(com.lelloman.simplerss.NavigationRouterTest.Companion.ACTIVITY, deepLink)
    }

    @Test
    fun `finds starter methods for WALKTHROUGH`() {
        val deepLink = DeepLink(com.lelloman.simplerss.navigation.SimpleRssNavigationScreen.WALKTHROUGH)
        com.lelloman.simplerss.navigation.SimpleRssNavigationScreen.WALKTHROUGH.deepLinkStartable = starter

        tested.handleDeepLink(com.lelloman.simplerss.NavigationRouterTest.Companion.ACTIVITY, DeepLinkNavigationEvent(deepLink))

        verify(starter).start(com.lelloman.simplerss.NavigationRouterTest.Companion.ACTIVITY, deepLink)
    }

    @Test
    fun `finds starter methods for ARTICLES_LIST`() {
        val deepLink = DeepLink(com.lelloman.simplerss.navigation.SimpleRssNavigationScreen.ARTICLES_LIST)
        com.lelloman.simplerss.navigation.SimpleRssNavigationScreen.ARTICLES_LIST.deepLinkStartable = starter

        tested.handleDeepLink(com.lelloman.simplerss.NavigationRouterTest.Companion.ACTIVITY, DeepLinkNavigationEvent(deepLink))

        verify(starter).start(com.lelloman.simplerss.NavigationRouterTest.Companion.ACTIVITY, deepLink)
    }

    @Test
    fun `finds starter methods for DISCOVER_URL`() {
        val deepLink = DeepLink(com.lelloman.simplerss.navigation.SimpleRssNavigationScreen.DISCOVER_URL)
        com.lelloman.simplerss.navigation.SimpleRssNavigationScreen.DISCOVER_URL.deepLinkStartable = starter

        tested.handleDeepLink(com.lelloman.simplerss.NavigationRouterTest.Companion.ACTIVITY, DeepLinkNavigationEvent(deepLink))

        verify(starter).start(com.lelloman.simplerss.NavigationRouterTest.Companion.ACTIVITY, deepLink)
    }

    @Test
    fun `finds starter methods for FOUND_FEED_LIST`() {
        val url = "www.meow.com"
        val deepLink = DeepLink(com.lelloman.simplerss.navigation.SimpleRssNavigationScreen.FOUND_FEED_LIST).putString(ARG_URL, url)
        com.lelloman.simplerss.navigation.SimpleRssNavigationScreen.FOUND_FEED_LIST.deepLinkStartable = starter

        tested.handleDeepLink(com.lelloman.simplerss.NavigationRouterTest.Companion.ACTIVITY, DeepLinkNavigationEvent(deepLink))

        verify(starter).start(com.lelloman.simplerss.NavigationRouterTest.Companion.ACTIVITY, deepLink)
    }

    private companion object {
        val ACTIVITY: Activity = object : Activity() {}
    }
}