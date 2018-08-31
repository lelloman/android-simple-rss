package com.lelloman.read.persistence.settings

import android.content.Context
import android.content.SharedPreferences
import com.lelloman.read.core.view.AppTheme
import com.lelloman.read.utils.Constants.AppSettings.DEFAULT_APP_THEME
import com.lelloman.read.utils.Constants.AppSettings.DEFAULT_ARTICLES_LIST_IMAGES
import com.lelloman.read.utils.Constants.AppSettings.DEFAULT_MIN_SOURCE_REFRESH_INTERVAL
import com.lelloman.read.utils.Constants.AppSettings.DEFAULT_OPEN_ARTICLES_IN_APP
import com.lelloman.read.utils.Constants.AppSettings.DEFAULT_SHOULD_SHOW_WALKTHROUGH
import com.lelloman.read.utils.Constants.AppSettings.DEFAULT_USE_METERED_NETWORK
import com.lelloman.read.utils.Constants.AppSettings.KEY_APP_THEME
import com.lelloman.read.utils.Constants.AppSettings.KEY_ARTICLE_LIST_IMAGES
import com.lelloman.read.utils.Constants.AppSettings.KEY_MIN_SOURCE_REFRESH_INTERVAL
import com.lelloman.read.utils.Constants.AppSettings.KEY_OPEN_ARTICLES_IN_APP
import com.lelloman.read.utils.Constants.AppSettings.KEY_SHOULD_SHOW_WALKTHROUGH
import com.lelloman.read.utils.Constants.AppSettings.KEY_USE_METERED_NETWORK
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Before
import org.junit.Test

class AppSettingsImplTest {

    private var prefInterval = SourceRefreshInterval.NEUROTIC
    private var prefArticlesImages = false
    private var prefUseMeteredNetwork = false
    private var prefOpenArticlesInApp = false
    private var prefShouldShowWalkthrough = false
    private var prefAppTheme = AppTheme.LIGHT

    private val sharedPrefsEditor: SharedPreferences.Editor = mock()

    private val sharedPrefs: SharedPreferences = mock {
        on { getString(KEY_MIN_SOURCE_REFRESH_INTERVAL, DEFAULT_MIN_SOURCE_REFRESH_INTERVAL.name) }
            .thenAnswer { prefInterval.name }

        on { getBoolean(KEY_ARTICLE_LIST_IMAGES, DEFAULT_ARTICLES_LIST_IMAGES) }
            .thenAnswer { prefArticlesImages }

        on { getBoolean(KEY_USE_METERED_NETWORK, DEFAULT_USE_METERED_NETWORK) }
            .thenAnswer { prefUseMeteredNetwork }

        on { getBoolean(KEY_OPEN_ARTICLES_IN_APP, DEFAULT_OPEN_ARTICLES_IN_APP) }
            .thenAnswer { prefOpenArticlesInApp }

        on { getBoolean(KEY_SHOULD_SHOW_WALKTHROUGH, DEFAULT_SHOULD_SHOW_WALKTHROUGH) }
            .thenAnswer { prefShouldShowWalkthrough }

        on { getString(KEY_APP_THEME, DEFAULT_APP_THEME.name) }
            .thenAnswer { prefAppTheme.name }

        on { edit() }.thenReturn(sharedPrefsEditor)
    }

    private val context: Context = mock {
        on { getSharedPreferences(any(), any()) }.thenReturn(sharedPrefs)
    }

    @Before
    fun setUp() {
        whenever(sharedPrefsEditor.putString(any(), any())).thenReturn(sharedPrefsEditor)
        whenever(sharedPrefsEditor.putBoolean(any(), any())).thenReturn(sharedPrefsEditor)
    }

    @Test
    fun `gets and sets min refresh interval`() {
        val firstInterval = SourceRefreshInterval.RELAXED
        val secondInterval = SourceRefreshInterval.STONER
        prefInterval = firstInterval
        val tested = AppSettingsImpl(context)
        val tester = tested.sourceRefreshMinInterval.test()

        tester.assertValue(firstInterval)

        prefInterval = secondInterval
        tested.setSourceRefreshMinInterval(secondInterval)

        verify(sharedPrefsEditor).putString(KEY_MIN_SOURCE_REFRESH_INTERVAL, secondInterval.name)
        tester.assertValues(firstInterval, secondInterval)
    }

    @Test
    fun `gets and sets articles list images enabled`() {
        prefArticlesImages = false
        val tested = AppSettingsImpl(context)
        val tester = tested.articleListImagesEnabled.test()

        tester.assertValue(false)

        prefArticlesImages = true
        tested.setArticlesListImagesEnabled(true)

        verify(sharedPrefsEditor).putBoolean(KEY_ARTICLE_LIST_IMAGES, true)
        tester.assertValues(false, true)
    }

    @Test
    fun `gets and sets use metered network`() {
        prefUseMeteredNetwork = false
        val tested = AppSettingsImpl(context)
        val tester = tested.useMeteredNetwork.test()

        tester.assertValue(false)

        prefUseMeteredNetwork = true
        tested.setUseMeteredNetwork(true)

        verify(sharedPrefsEditor).putBoolean(KEY_USE_METERED_NETWORK, true)
        tester.assertValues(false, true)
    }

    @Test
    fun `gets and sets open articles in app`() {
        prefOpenArticlesInApp = false
        val tested = AppSettingsImpl(context)
        val tester = tested.openArticlesInApp.test()

        tester.assertValues(false)

        prefOpenArticlesInApp = true
        tested.setOpenArticlesInApp(true)

        verify(sharedPrefsEditor).putBoolean(KEY_OPEN_ARTICLES_IN_APP, true)
        tester.assertValues(false, true)
    }

    @Test
    fun `gets and sets should show walkthrough`() {
        prefShouldShowWalkthrough = false
        val tested = AppSettingsImpl(context)
        val tester = tested.shouldShowWalkthrough.test()

        tester.assertValues(false)

        prefShouldShowWalkthrough = true
        tested.setShouldShowWalkthtough(true)

        verify(sharedPrefsEditor).putBoolean(KEY_SHOULD_SHOW_WALKTHROUGH, true)
        tester.assertValues(false, true)
    }

    @Test
    fun `gets and sets app theme`(){
        prefAppTheme = AppTheme.DARCULA
        val tested = AppSettingsImpl(context)
        val tester = tested.appTheme.test()

        tester.assertValues(AppTheme.DARCULA)

        prefAppTheme = AppTheme.LIGHT
        tested.setAppTheme(AppTheme.LIGHT)

        verify(sharedPrefsEditor).putString(KEY_APP_THEME, AppTheme.LIGHT.name)
        tester.assertValues(AppTheme.DARCULA, AppTheme.LIGHT)
    }
}