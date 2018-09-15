package com.lelloman.read.core.navigation

import android.app.Activity
import com.lelloman.read.core.navigation.NavigationScreen.Companion.ARG_URL
import com.lelloman.read.mock.MockLoggerFactory
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.junit.Test

class NavigationRouterTest {

    private val tested = NavigationRouter(MockLoggerFactory())

    private val starter: DeepLinkStartable = mock()

    @Test
    fun `finds starter methods for SOURCES_LIST`() {
        val deepLink = DeepLink(NavigationScreen.SOURCES_LIST)
        NavigationScreen.SOURCES_LIST.deepLinkStartable = starter

        tested.handleDeepLink(ACTIVITY, DeepLinkNavigationEvent(deepLink))

        verify(starter).start(ACTIVITY, deepLink)
    }

    @Test
    fun `finds starter methods for ADD_SOURCE`() {
        val deepLink = DeepLink(NavigationScreen.ADD_SOURCE)
        NavigationScreen.ADD_SOURCE.deepLinkStartable = starter

        tested.handleDeepLink(ACTIVITY, DeepLinkNavigationEvent(deepLink))

        verify(starter).start(ACTIVITY, deepLink)
    }

    @Test
    fun `finds starter methods for ARTICLE`() {
        val url = "www.meow.com"
        val deepLink = DeepLink(NavigationScreen.ARTICLE).putString(ARG_URL, url)
        NavigationScreen.ARTICLE.deepLinkStartable = starter

        tested.handleDeepLink(ACTIVITY, DeepLinkNavigationEvent(deepLink))

        verify(starter).start(ACTIVITY, deepLink)
    }

    @Test
    fun `finds starter methods for SETTINGS`() {
        val deepLink = DeepLink(NavigationScreen.SETTINGS)
        NavigationScreen.SETTINGS.deepLinkStartable = starter

        tested.handleDeepLink(ACTIVITY, DeepLinkNavigationEvent(deepLink))

        verify(starter).start(ACTIVITY, deepLink)
    }

    @Test
    fun `finds starter methods for WALKTHROUGH`() {
        val deepLink = DeepLink(NavigationScreen.WALKTHROUGH)
        NavigationScreen.WALKTHROUGH.deepLinkStartable = starter

        tested.handleDeepLink(ACTIVITY, DeepLinkNavigationEvent(deepLink))

        verify(starter).start(ACTIVITY, deepLink)
    }

    @Test
    fun `finds starter methods for ARTICLES_LIST`() {
        val deepLink = DeepLink(NavigationScreen.ARTICLES_LIST)
        NavigationScreen.ARTICLES_LIST.deepLinkStartable = starter

        tested.handleDeepLink(ACTIVITY, DeepLinkNavigationEvent(deepLink))

        verify(starter).start(ACTIVITY, deepLink)
    }

    @Test
    fun `finds starter methods for DISCOVER_URL`() {
        val deepLink = DeepLink(NavigationScreen.DISCOVER_URL)
        NavigationScreen.DISCOVER_URL.deepLinkStartable = starter

        tested.handleDeepLink(ACTIVITY, DeepLinkNavigationEvent(deepLink))

        verify(starter).start(ACTIVITY, deepLink)
    }

    @Test
    fun `finds starter methods for FOUND_FEED_LIST`() {
        val url = "www.meow.com"
        val deepLink = DeepLink(NavigationScreen.FOUND_FEED_LIST).putString(ARG_URL, url)
        NavigationScreen.FOUND_FEED_LIST.deepLinkStartable = starter

        tested.handleDeepLink(ACTIVITY, DeepLinkNavigationEvent(deepLink))

        verify(starter).start(ACTIVITY, deepLink)
    }

    private companion object {
        val ACTIVITY: Activity = object : Activity() {}
    }
}