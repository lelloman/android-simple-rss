package com.lelloman.simplerss.ui.debug.viewmodel

import com.lelloman.common.viewmodel.BaseViewModel
import com.lelloman.simplerss.navigation.SimpleRssNavigationScreen
import com.lelloman.simplerss.persistence.db.AppDatabase
import com.lelloman.simplerss.persistence.settings.AppSettings
import io.reactivex.Completable

class DebugViewModel(
    dependencies: Dependencies,
    private val appSettings: AppSettings,
    private val appDatabase: AppDatabase
) : BaseViewModel(dependencies) {

    fun onResetSharedPrefsClicked() = navigate(SimpleRssNavigationScreen.RESET_SHARED_PREFS_CONFIRMATION)

    fun onResetDbClicked() = navigate(SimpleRssNavigationScreen.RESET_DB_CONFIRMATION)

    fun onResetSharedPrefsConfirmed() = appSettings.reset()

    fun onResetDbConfirmed() {
        Completable
            .fromAction {
                appDatabase.clearAllTables()
            }
            .subscribeOn(ioScheduler)
            .subscribe()
    }
}