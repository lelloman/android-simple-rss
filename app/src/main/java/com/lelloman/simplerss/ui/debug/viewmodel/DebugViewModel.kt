package com.lelloman.simplerss.ui.debug.viewmodel

import com.lelloman.common.viewmodel.BaseViewModel
import com.lelloman.simplerss.persistence.db.AppDatabase
import com.lelloman.simplerss.persistence.settings.AppSettings
import com.lelloman.simplerss.ui.OpenResetDbScreenCommand
import com.lelloman.simplerss.ui.OpenResetSharedPreferencesScreenCommand
import io.reactivex.Completable

class DebugViewModel(
    dependencies: Dependencies,
    private val appSettings: AppSettings,
    private val appDatabase: AppDatabase
) : BaseViewModel(dependencies) {

    fun onResetSharedPrefsClicked() = emitCommand(OpenResetSharedPreferencesScreenCommand)

    fun onResetDbClicked() = emitCommand(OpenResetDbScreenCommand)

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