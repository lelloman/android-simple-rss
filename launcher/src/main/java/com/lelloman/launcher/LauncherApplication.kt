package com.lelloman.launcher

import android.app.Activity
import com.lelloman.common.BaseApplication
import com.lelloman.common.utils.TimeProvider
import com.lelloman.launcher.classification.PackageClassifier
import com.lelloman.launcher.di.DaggerAppComponent
import com.lelloman.launcher.di.LauncherBaseApplicationModule
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

open class LauncherApplication : BaseApplication(), HasActivityInjector {

    @Inject
    lateinit var dispatchingActivityAndroidInjector: DispatchingAndroidInjector<Activity>

    @Inject
    lateinit var packageClassifier: PackageClassifier

    @Inject
    lateinit var timeProvider: TimeProvider

    private val sharedPrefs by lazy { getSharedPreferences(SHARED_PREFS_NAME, MODE_PRIVATE) }

    private var lastClassificationTimeMs: Long
        get() = sharedPrefs.getLong(KEY_LAST_CLASSIFICATION_TIME_MS, 0L)
        set(value) = sharedPrefs
            .edit()
            .putLong(KEY_LAST_CLASSIFICATION_TIME_MS, value)
            .apply()

    override fun activityInjector() = dispatchingActivityAndroidInjector

    override fun inject() = DaggerAppComponent
        .builder()
        .baseApplicationModule(LauncherBaseApplicationModule(this))
        .build()
        .inject(this)

    override fun onCreate() {
        super.onCreate()
        @Suppress("UNUSED_VARIABLE")
        val unused = Observable
            .interval(30, TimeUnit.MINUTES)
            .observeOn(Schedulers.io())
            .subscribe {
                maybeTriggerPackageClassification()
            }
        maybeTriggerPackageClassification()
    }

    private fun maybeTriggerPackageClassification() {
        val timeSinceLastClassification = timeProvider.nowUtcMs() - lastClassificationTimeMs
        if (timeSinceLastClassification > 1000L * 60 * 45) {
            packageClassifier.classifyWithNeuralNet()
            lastClassificationTimeMs = timeProvider.nowUtcMs()
        }
    }

    private companion object {
        const val SHARED_PREFS_NAME = "LLLauncherPrefs"
        const val KEY_LAST_CLASSIFICATION_TIME_MS = "LastClassificationTimeMs"
    }
}