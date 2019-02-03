package com.lelloman.simplerss.unit

import android.support.test.InstrumentationRegistry
import android.support.test.InstrumentationRegistry.getContext
import android.support.test.runner.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.lelloman.common.settings.BaseApplicationSettings
import com.lelloman.common.settings.BaseApplicationSettings.Companion.DEFAULT_APP_THEME
import com.lelloman.common.settings.BaseApplicationSettings.Companion.DEFAULT_USE_METERED_NETWORK
import com.lelloman.common.settings.BaseSettingsModule
import com.lelloman.common.view.AppTheme
import com.lelloman.simplerss.persistence.settings.AppSettings.Companion.DEFAULT_ARTICLES_LIST_IMAGES
import com.lelloman.simplerss.persistence.settings.AppSettings.Companion.DEFAULT_MIN_SOURCE_REFRESH_INTERVAL
import com.lelloman.simplerss.persistence.settings.AppSettings.Companion.DEFAULT_OPEN_ARTICLES_IN_APP
import com.lelloman.simplerss.persistence.settings.AppSettings.Companion.DEFAULT_SHOULD_SHOW_WALKTHROUGH
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppSettingsImplTest {

    private val baseApplicationSettings: BaseApplicationSettings = BaseSettingsModule().provideBaseApplicationSettings(getContext())

    private fun tested(action: com.lelloman.simplerss.persistence.settings.AppSettingsImpl.() -> Unit) = com.lelloman.simplerss.persistence.settings.AppSettingsImpl(getContext(), baseApplicationSettings).run(action)

    private val nonDefaultRefreshInterval = com.lelloman.simplerss.persistence.settings.SourceRefreshInterval.values().first { it != com.lelloman.simplerss.persistence.settings.AppSettings.DEFAULT_MIN_SOURCE_REFRESH_INTERVAL }
    private val nonDefaultArticlesListImages = DEFAULT_ARTICLES_LIST_IMAGES.not()
    private val nonDefaultUseMeteredNetwork = DEFAULT_USE_METERED_NETWORK.not()
    private val nonDefaultOpenArticlesInApp = DEFAULT_OPEN_ARTICLES_IN_APP.not()
    private val nonDefaultShouldShowWalkthrough = DEFAULT_SHOULD_SHOW_WALKTHROUGH.not()
    private val nonDefaultAppTheme = AppTheme.values().first { it != DEFAULT_APP_THEME }

    @Before
    fun setUp() {
        InstrumentationRegistry
            .getContext()
            .getSharedPreferences(com.lelloman.simplerss.persistence.settings.AppSettings.SHARED_PREFS_NAME, 0)
            .edit()
            .clear()
            .commit()
    }

    @Test
    fun returnsDefaultValues() {
        assertDefaultValues()
    }

    @Test
    fun setsAndGetsSourceRefreshMinInterval() {
        tested {
            val tester = sourceRefreshMinInterval.test()
            tester.assertValues(DEFAULT_MIN_SOURCE_REFRESH_INTERVAL)

            setSourceRefreshMinInterval(nonDefaultRefreshInterval)

            tester.assertValues(DEFAULT_MIN_SOURCE_REFRESH_INTERVAL, nonDefaultRefreshInterval)
        }

        tested { sourceRefreshMinInterval.test().assertValues(nonDefaultRefreshInterval) }
    }

    @Test
    fun setsAndGetsArticlesListImages() {
        tested {
            val tester = articleListImagesEnabled.test()
            tester.assertValues(DEFAULT_ARTICLES_LIST_IMAGES)

            setArticlesListImagesEnabled(nonDefaultArticlesListImages)

            tester.assertValues(DEFAULT_ARTICLES_LIST_IMAGES, nonDefaultArticlesListImages)
        }

        tested { articleListImagesEnabled.test().assertValues(nonDefaultArticlesListImages) }
    }

    @Test
    fun setsAndGetsUseMeteredNetwork() {
        tested {
            val tester = useMeteredNetwork.test()
            tester.assertValues(DEFAULT_USE_METERED_NETWORK)

            setUseMeteredNetwork(nonDefaultUseMeteredNetwork)

            tester.assertValues(DEFAULT_USE_METERED_NETWORK, nonDefaultUseMeteredNetwork)
        }

        tested { useMeteredNetwork.test().assertValues(nonDefaultUseMeteredNetwork) }
    }

    @Test
    fun setsAndGetsOpenArticlesInApp() {
        tested {
            val tester = openArticlesInApp.test()
            tester.assertValues(DEFAULT_OPEN_ARTICLES_IN_APP)

            setOpenArticlesInApp(nonDefaultOpenArticlesInApp)

            tester.assertValues(DEFAULT_OPEN_ARTICLES_IN_APP, nonDefaultOpenArticlesInApp)
        }

        tested { openArticlesInApp.test().assertValues(nonDefaultOpenArticlesInApp) }
    }

    @Test
    fun setsAndGetsShouldShowWalkthrough() {
        tested {
            val tester = shouldShowWalkthrough.test()
            tester.assertValues(DEFAULT_SHOULD_SHOW_WALKTHROUGH)

            setShouldShowWalkthtough(nonDefaultShouldShowWalkthrough)

            tester.assertValues(DEFAULT_SHOULD_SHOW_WALKTHROUGH, nonDefaultShouldShowWalkthrough)
        }

        tested { shouldShowWalkthrough.test().assertValues(nonDefaultShouldShowWalkthrough) }
    }

    @Test
    fun setsAndGetsAppTheme() {
        tested {
            val tester = appTheme.test()
            tester.assertValues(DEFAULT_APP_THEME)

            setAppTheme(nonDefaultAppTheme)

            tester.assertValues(DEFAULT_APP_THEME, nonDefaultAppTheme)
        }

        tested { appTheme.test().assertValues(nonDefaultAppTheme) }
    }

    @Test
    fun resetsValuesToDefault() {
        tested {
            setSourceRefreshMinInterval(nonDefaultRefreshInterval)
            setArticlesListImagesEnabled(nonDefaultArticlesListImages)
            setUseMeteredNetwork(nonDefaultUseMeteredNetwork)
            setOpenArticlesInApp(nonDefaultOpenArticlesInApp)
            setShouldShowWalkthtough(nonDefaultShouldShowWalkthrough)
            setAppTheme(nonDefaultAppTheme)
        }

        tested { reset() }

        tested { assertDefaultValues() }
    }

    private fun assertDefaultValues() = tested {
        assertThat(sourceRefreshMinInterval.blockingFirst()).isEqualTo(DEFAULT_MIN_SOURCE_REFRESH_INTERVAL)
        assertThat(articleListImagesEnabled.blockingFirst()).isEqualTo(DEFAULT_ARTICLES_LIST_IMAGES)
        assertThat(useMeteredNetwork.blockingFirst()).isEqualTo(DEFAULT_USE_METERED_NETWORK)
        assertThat(openArticlesInApp.blockingFirst()).isEqualTo(DEFAULT_OPEN_ARTICLES_IN_APP)
        assertThat(shouldShowWalkthrough.blockingFirst()).isEqualTo(DEFAULT_SHOULD_SHOW_WALKTHROUGH)
        assertThat(appTheme.blockingFirst()).isEqualTo(DEFAULT_APP_THEME)
    }
}