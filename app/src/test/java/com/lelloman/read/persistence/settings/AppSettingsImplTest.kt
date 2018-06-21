package com.lelloman.read.persistence.settings

import android.content.Context
import android.content.SharedPreferences
import com.lelloman.read.utils.Constants.AppSettings.DEFAULT_ARTICLES_LIST_IMAGES
import com.lelloman.read.utils.Constants.AppSettings.DEFAULT_MIN_SOURCE_REFRESH_INTERVAL
import com.lelloman.read.utils.Constants.AppSettings.DEFAULT_USE_METERED_NETWORK
import com.lelloman.read.utils.Constants.AppSettings.KEY_ARTICLE_LIST_IMAGES
import com.lelloman.read.utils.Constants.AppSettings.KEY_MIN_SOURCE_REFRESH_INTERVAL
import com.lelloman.read.utils.Constants.AppSettings.KEY_USE_METERED_NETWORK
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import org.junit.Test

class AppSettingsImplTest {

    private var prefInterval = SourceRefreshInterval.NEUROTIC
    private var prefArticlesImages = false
    private var prefUseMeteredNetwork = false

    private val sharedPrefs: SharedPreferences = mock {
        on { getString(KEY_MIN_SOURCE_REFRESH_INTERVAL, DEFAULT_MIN_SOURCE_REFRESH_INTERVAL.name) }
            .thenAnswer { prefInterval.name }

        on { getBoolean(KEY_ARTICLE_LIST_IMAGES, DEFAULT_ARTICLES_LIST_IMAGES) }
            .thenAnswer { prefArticlesImages }

        on { getBoolean(KEY_USE_METERED_NETWORK, DEFAULT_USE_METERED_NETWORK) }
            .thenAnswer { prefUseMeteredNetwork }
    }

    private val context: Context = mock {
        on { getSharedPreferences(any(), any()) }.thenReturn(sharedPrefs)
    }

    private val tested = AppSettingsImpl(context)

    @Test
    fun `can get and set min refresh interval`() {
        tested.sourceRefreshMinInterval.test().assertValue(prefInterval)
    }
}