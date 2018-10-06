package com.lelloman.read

import android.app.Activity
import com.lelloman.common.navigation.DeepLink
import com.lelloman.common.navigation.DeepLinkNavigationEvent
import com.lelloman.common.navigation.DeepLinkStartable
import com.lelloman.common.navigation.NavigationRouter
import com.lelloman.common.testutils.MockLoggerFactory
import com.lelloman.read.navigation.ReadNavigationScreen
import com.lelloman.read.navigation.ReadNavigationScreen.Companion.ARG_URL
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.junit.Test

class NavigationRouterTest {

    private val applicationPackageName = "cra cra cra"
    private val tested = NavigationRouter(
        packageManager = mock(),
        loggerFactory = MockLoggerFactory(),
        applicationPackageName = applicationPackageName
    )

    private val starter: DeepLinkStartable = mock()

    @Test
    fun `finds starter methods for SOURCES_LIST`() {
        val deepLink = DeepLink(ReadNavigationScreen.SOURCES_LIST)
        ReadNavigationScreen.SOURCES_LIST.deepLinkStartable = starter

        tested.handleDeepLink(ACTIVITY, DeepLinkNavigationEvent(deepLink))

        verify(starter).start(ACTIVITY, deepLink)
    }

    @Test
    fun `finds starter methods for ADD_SOURCE`() {
        val deepLink = DeepLink(ReadNavigationScreen.ADD_SOURCE)
        ReadNavigationScreen.ADD_SOURCE.deepLinkStartable = starter

        tested.handleDeepLink(ACTIVITY, DeepLinkNavigationEvent(deepLink))

        verify(starter).start(ACTIVITY, deepLink)
    }

    @Test
    fun `finds starter methods for ARTICLE`() {
        val url = "www.meow.com"
        val deepLink = DeepLink(ReadNavigationScreen.ARTICLE).putString(ARG_URL, url)
        ReadNavigationScreen.ARTICLE.deepLinkStartable = starter

        tested.handleDeepLink(ACTIVITY, DeepLinkNavigationEvent(deepLink))

        verify(starter).start(ACTIVITY, deepLink)
    }

    @Test
    fun `finds starter methods for SETTINGS`() {
        val deepLink = DeepLink(ReadNavigationScreen.SETTINGS)
        ReadNavigationScreen.SETTINGS.deepLinkStartable = starter

        tested.handleDeepLink(ACTIVITY, DeepLinkNavigationEvent(deepLink))

        verify(starter).start(ACTIVITY, deepLink)
    }

    @Test
    fun `finds starter methods for WALKTHROUGH`() {
        val deepLink = DeepLink(ReadNavigationScreen.WALKTHROUGH)
        ReadNavigationScreen.WALKTHROUGH.deepLinkStartable = starter

        tested.handleDeepLink(ACTIVITY, DeepLinkNavigationEvent(deepLink))

        verify(starter).start(ACTIVITY, deepLink)
    }

    @Test
    fun `finds starter methods for ARTICLES_LIST`() {
        val deepLink = DeepLink(ReadNavigationScreen.ARTICLES_LIST)
        ReadNavigationScreen.ARTICLES_LIST.deepLinkStartable = starter

        tested.handleDeepLink(ACTIVITY, DeepLinkNavigationEvent(deepLink))

        verify(starter).start(ACTIVITY, deepLink)
    }

    @Test
    fun `finds starter methods for DISCOVER_URL`() {
        val deepLink = DeepLink(ReadNavigationScreen.DISCOVER_URL)
        ReadNavigationScreen.DISCOVER_URL.deepLinkStartable = starter

        tested.handleDeepLink(ACTIVITY, DeepLinkNavigationEvent(deepLink))

        verify(starter).start(ACTIVITY, deepLink)
    }

    @Test
    fun `finds starter methods for FOUND_FEED_LIST`() {
        val url = "www.meow.com"
        val deepLink = DeepLink(ReadNavigationScreen.FOUND_FEED_LIST).putString(ARG_URL, url)
        ReadNavigationScreen.FOUND_FEED_LIST.deepLinkStartable = starter

        tested.handleDeepLink(ACTIVITY, DeepLinkNavigationEvent(deepLink))

        verify(starter).start(ACTIVITY, deepLink)
    }

    private companion object {
        val ACTIVITY: Activity = object : Activity() {}
    }
}