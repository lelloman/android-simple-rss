package com.lelloman.read.unit

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.lelloman.read.core.view.AppTheme
import com.lelloman.read.persistence.settings.AppSettingsImpl
import com.lelloman.read.persistence.settings.SourceRefreshInterval
import com.lelloman.read.utils.Constants
import com.lelloman.read.utils.Constants.AppSettings.DEFAULT_APP_THEME
import com.lelloman.read.utils.Constants.AppSettings.DEFAULT_ARTICLES_LIST_IMAGES
import com.lelloman.read.utils.Constants.AppSettings.DEFAULT_MIN_SOURCE_REFRESH_INTERVAL
import com.lelloman.read.utils.Constants.AppSettings.DEFAULT_OPEN_ARTICLES_IN_APP
import com.lelloman.read.utils.Constants.AppSettings.DEFAULT_SHOULD_SHOW_WALKTHROUGH
import com.lelloman.read.utils.Constants.AppSettings.DEFAULT_USE_METERED_NETWORK
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppSettingsImplTest {

    private fun tested(action: AppSettingsImpl.() -> Unit) = AppSettingsImpl(InstrumentationRegistry.getContext()).run(action)

    private val nonDefaultRefreshInterval = SourceRefreshInterval.values().first { it != DEFAULT_MIN_SOURCE_REFRESH_INTERVAL }
    private val nonDefaultArticlesListImages = DEFAULT_ARTICLES_LIST_IMAGES.not()
    private val nonDefaultUseMeteredNetwork = DEFAULT_USE_METERED_NETWORK.not()
    private val nonDefaultOpenArticlesInApp = DEFAULT_OPEN_ARTICLES_IN_APP.not()
    private val nonDefaultShouldShowWalkthrough = DEFAULT_SHOULD_SHOW_WALKTHROUGH.not()
    private val nonDefaultAppTheme = AppTheme.values().first { it != DEFAULT_APP_THEME }

    @Before
    fun setUp() {
        InstrumentationRegistry
            .getContext()
            .getSharedPreferences(Constants.AppSettings.SHARED_PREFS_NAME, 0)
            .edit()
            .clear()
            .commit()
    }

    @Test
    fun returnsDefaultValues() {
        assertDefaultValues()
    }

    private fun assertDefaultValues() = tested {
        assertThat(sourceRefreshMinInterval.blockingFirst()).isEqualTo(DEFAULT_MIN_SOURCE_REFRESH_INTERVAL)
        assertThat(articleListImagesEnabled.blockingFirst()).isEqualTo(DEFAULT_ARTICLES_LIST_IMAGES)
        assertThat(useMeteredNetwork.blockingFirst()).isEqualTo(DEFAULT_USE_METERED_NETWORK)
        assertThat(openArticlesInApp.blockingFirst()).isEqualTo(DEFAULT_OPEN_ARTICLES_IN_APP)
        assertThat(shouldShowWalkthrough.blockingFirst()).isEqualTo(DEFAULT_SHOULD_SHOW_WALKTHROUGH)
        assertThat(appTheme.blockingFirst()).isEqualTo(DEFAULT_APP_THEME)
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
}