package com.lelloman.simplerss.ui.settings.viewmodel

import com.lelloman.common.jvmtestutils.MockLoggerFactory
import com.lelloman.common.logger.Logger
import com.lelloman.common.view.FileProvider
import com.lelloman.common.view.SemanticTimeProvider
import com.lelloman.common.viewmodel.BaseViewModel
import com.lelloman.simplerss.persistence.db.AppDatabase
import com.lelloman.simplerss.persistence.settings.AppSettings
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.argThat
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers.trampoline
import org.junit.Test

class SettingsViewModelImplTest {

    private val appSettings: AppSettings = mock()
    private val semanticTimeProvider: SemanticTimeProvider = mock()
    private val appDatabase: AppDatabase = mock()
    private val fileProvider: FileProvider = mock {
        on { deleteAllCacheFiles() }.thenReturn(Completable.complete())
        on { deleteAllInternalFiles() }.thenReturn(Completable.complete())
    }
    private val logger: Logger = mock()
    private val dependencies = BaseViewModel.Dependencies(
        settings = appSettings,
        resourceProvider = mock(),
        ioScheduler = trampoline(),
        uiScheduler = trampoline(),
        actionTokenProvider = mock(),
        loggerFactory = MockLoggerFactory(logger)
    )

    private val tested = SettingsViewModelImpl(
        appSettings = appSettings,
        semanticTimeProvider = semanticTimeProvider,
        appDatabase = appDatabase,
        fileProvider = fileProvider,
        dependencies = dependencies
    )

    @Test
    fun `clears db and files when data clear is confirmed`() {
        tested.onClearDataConfirmed()

        verify(appDatabase).clearAllTables()
        verify(fileProvider).deleteAllCacheFiles()
        verify(fileProvider).deleteAllInternalFiles()
    }

    @Test
    fun `swallows and logs app database error when clearing data`() {
        val exception = Exception("woof")
        whenever(appDatabase.clearAllTables()).thenAnswer { throw exception }

        tested.onClearDataConfirmed()

        verify(fileProvider).deleteAllCacheFiles()
        verify(fileProvider).deleteAllInternalFiles()
        verify(logger).e(any(), argThat { this === exception })
    }

    @Test
    fun `swallows and logs delete cache files error when clearing data`() {
        val exception = Exception("woof")
        whenever(fileProvider.deleteAllCacheFiles()).thenReturn(Completable.error(exception))

        tested.onClearDataConfirmed()

        verify(appDatabase).clearAllTables()
        verify(fileProvider).deleteAllInternalFiles()
        verify(logger).e(any(), argThat { this === exception })
    }

    @Test
    fun `swallows and logs delete internal files error when clearing data`() {
        val exception = Exception("woof")
        whenever(fileProvider.deleteAllInternalFiles()).thenReturn(Completable.error(exception))

        tested.onClearDataConfirmed()

        verify(appDatabase).clearAllTables()
        verify(fileProvider).deleteAllCacheFiles()
        verify(logger).e(any(), argThat { this === exception })
    }
}