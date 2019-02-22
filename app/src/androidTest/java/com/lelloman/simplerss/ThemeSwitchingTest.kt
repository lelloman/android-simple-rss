package com.lelloman.simplerss

import android.app.Application
import android.util.TypedValue
import androidx.test.core.app.ApplicationProvider
import androidx.test.rule.ActivityTestRule
import com.google.common.truth.Truth.assertThat
import com.lelloman.common.view.AppTheme
import com.lelloman.simplerss.testutils.screen.WalkthroughScreen
import com.lelloman.simplerss.testutils.setUpTestAppWithMockedHttpStack
import com.lelloman.simplerss.ui.launcher.view.LauncherActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ThemeSwitchingTest {

    @get:Rule
    val activityTestRule = ActivityTestRule(LauncherActivity::class.java, true, false)

    @Before
    fun setUp() {
        setUpTestAppWithMockedHttpStack { true }
        activityTestRule.launchActivity(null)
    }

    @Test
    fun switchesThemesInWalkthrough() {
        WalkthroughScreen()
            .swipeRight()
            .swipeRight()
            .swipeRight()
            .assertPagerIndicatorColor(LIGHT_ACCENT_COLOR)
            .clickOnTheme(AppTheme.DARCULA)
            .assertPagerIndicatorColor(DARCULA_ACCENT_COLOR)
            .clickOnTheme(AppTheme.MOCKITO)
            .assertPagerIndicatorColor(MOCKITO_ACCENT_COLOR)
    }

    @Test
    fun switchesThemesInSettings() {
        WalkthroughScreen()
            .pressClose()
            .clickOnSettingsInOverflow()
            .assertPrimaryColor(LIGHT_PRIMARY_COLOR)
            .setTheme(AppTheme.DARCULA)
            .assertPrimaryColor(DARCULA_PRIMARY_COLOR)
            .setTheme(AppTheme.FOREST)
            .assertPrimaryColor(FOREST_PRIMARY_COLOR)
    }

    private companion object {

        fun getThemeAttr(appTheme: AppTheme, attr: Int): Int {
            val theme = ApplicationProvider
                .getApplicationContext<Application>()
                .resources
                .newTheme()
            theme.applyStyle(appTheme.resId, true)
            val typedValue = TypedValue()
            assertThat(theme.resolveAttribute(attr, typedValue, true)).isTrue()
            return typedValue.data
        }

        fun getColorAccent(appTheme: AppTheme) = getThemeAttr(appTheme, R.attr.colorAccent)
        fun getColorPrimary(appTheme: AppTheme) = getThemeAttr(appTheme, R.attr.colorPrimary)

        val LIGHT_ACCENT_COLOR by lazy { getColorAccent(AppTheme.LIGHT) }
        val DARCULA_ACCENT_COLOR by lazy { getColorAccent(AppTheme.DARCULA) }
        val MOCKITO_ACCENT_COLOR by lazy { getColorAccent(AppTheme.MOCKITO) }

        val LIGHT_PRIMARY_COLOR by lazy { getColorPrimary(AppTheme.LIGHT) }
        val DARCULA_PRIMARY_COLOR by lazy { getColorPrimary(AppTheme.DARCULA) }
        val FOREST_PRIMARY_COLOR by lazy { getColorPrimary(AppTheme.FOREST) }
    }
}