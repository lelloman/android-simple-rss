package com.lelloman.launcher.persistence

import android.content.Context
import com.lelloman.nn.Network

class ClassifierPersistenceImpl(context: Context) : ClassifierPersistence {

    private val sharedPrefs = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)

    override var lastClassificationTimeMs: Long
        get() = sharedPrefs.getLong(KEY_LAST_CLASSIFICATION_TIME_MS, 0L)
        set(value) = sharedPrefs
            .edit()
            .putLong(KEY_LAST_CLASSIFICATION_TIME_MS, value)
            .apply()

    override fun saveClassifier(network: Network) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadClassifier(): Network? {
        return null
    }

    private companion object {
        const val SHARED_PREFS_NAME = "ClassifierPersistence"
        const val KEY_LAST_CLASSIFICATION_TIME_MS = "LastClassificationTimeMs"
    }
}