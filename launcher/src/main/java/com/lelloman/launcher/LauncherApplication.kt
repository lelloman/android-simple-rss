package com.lelloman.launcher

import android.app.Activity
import com.lelloman.common.BaseApplication
import com.lelloman.common.settings.BaseApplicationSettings
import com.lelloman.common.utils.TimeProvider
import com.lelloman.common.viewmodel.ViewModelFactory
import com.lelloman.launcher.classification.ClassificationTrigger
import com.lelloman.launcher.di.DaggerAppComponent
import com.lelloman.launcher.di.LauncherBaseApplicationModule
import com.lelloman.launcher.logger.LauncherLoggerFactory
import com.lelloman.launcher.logger.ShouldLogToFile
import com.lelloman.launcher.persistence.db.AppDatabase
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

open class LauncherApplication : BaseApplication(), HasActivityInjector, ShouldLogToFile {

    @Inject
    lateinit var dispatchingActivityAndroidInjector: DispatchingAndroidInjector<Activity>

    @Inject
    lateinit var timeProvider: TimeProvider

    @Inject
    lateinit var loggerFactory: LauncherLoggerFactory

    @Inject
    lateinit var classificationTrigger: ClassificationTrigger

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var appSettings: BaseApplicationSettings

    @Inject
    lateinit var appDatabase: AppDatabase

    private val logger by lazy { loggerFactory.getLogger(javaClass) }

    override fun activityInjector() = dispatchingActivityAndroidInjector

    override fun inject() = DaggerAppComponent
        .builder()
        .baseApplicationModule(LauncherBaseApplicationModule(this))
        .build()
        .inject(this)

    override fun onCreate() {
        super.onCreate()
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            logger.e("Uncaught exception in thread $thread", throwable)
            System.exit(2)
        }
        startIntervalTimers()
        logger.d("onCreate()")
        classificationTrigger.start()
    }

    @Suppress("UNUSED_VARIABLE")
    private fun startIntervalTimers() {
        val anotherUnused = Observable
            .interval(5, TimeUnit.MINUTES)
            .observeOn(Schedulers.io())
            .subscribe {
                logger.d("ping")
            }
    }
}