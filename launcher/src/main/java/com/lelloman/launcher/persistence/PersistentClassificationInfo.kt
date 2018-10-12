package com.lelloman.launcher.persistence

import android.content.Context

class PersistentClassificationInfo(
    context: Context
) {

    private val sharedPrefs = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)

    var lastTrainingTimeMs: Long
        get() = sharedPrefs.getLong(KEY_LAST_TRAINING_TIME_MS, 0L)
        set(value) = sharedPrefs
            .edit()
            .putLong(KEY_LAST_TRAINING_TIME_MS, value)
            .apply()

    private companion object {
        const val SHARED_PREFS_NAME = "ClassificationInfo"
        const val KEY_LAST_TRAINING_TIME_MS = "LastTrainingTimeMs"
    }
}