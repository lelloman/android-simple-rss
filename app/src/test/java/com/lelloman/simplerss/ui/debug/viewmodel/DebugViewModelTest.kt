package com.lelloman.simplerss.ui.debug.viewmodel

import com.lelloman.common.jvmtestutils.MockLoggerFactory
import com.lelloman.common.navigation.DeepLinkNavigationEvent
import com.lelloman.common.viewmodel.BaseViewModel
import com.lelloman.simplerss.navigation.SimpleRssNavigationScreen
import com.lelloman.simplerss.persistence.db.AppDatabase
import com.lelloman.simplerss.persistence.settings.AppSettings
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import io.reactivex.schedulers.Schedulers.trampoline
import org.junit.Test

class DebugViewModelTest {

    private val appSettings: AppSettings = mock()
    private val appDatabase: AppDatabase = mock()

    private val tested = DebugViewModel(
        dependencies = BaseViewModel.Dependencies(
            settings = mock(),
            resourceProvider = mock(),
            actionTokenProvider = mock(),
            ioScheduler = trampoline(),
            uiScheduler = trampoline(),
            loggerFactory = MockLoggerFactory()
        ),
        appSettings = appSettings,
        appDatabase = appDatabase
    )

    @Test
    fun `navigate to reset db confirmation screen when reset db is clicked`() {
        val tester = tested.viewActionEvents.test()

        tested.onResetDbClicked()

        tester.assertValue {
            it is DeepLinkNavigationEvent && it.deepLink.screen == SimpleRssNavigationScreen.RESET_DB_CONFIRMATION
        }
    }

    @Test
    fun `navigate to reset shared prefs confirmation screen when reset shared prefs is clicked`() {
        val tester = tested.viewActionEvents.test()

        tested.onResetSharedPrefsClicked()

        tester.assertValue {
            it is DeepLinkNavigationEvent && it.deepLink.screen == SimpleRssNavigationScreen.RESET_SHARED_PREFS_CONFIRMATION
        }
    }

    @Test
    fun `resets shared prefs when confirmed`() {
        tested.onResetSharedPrefsConfirmed()

        verify(appSettings).reset()
    }

    @Test
    fun `resets db when confirmed`() {
        tested.onResetDbConfirmed()

        verify(appDatabase).clearAllTables()
    }
}