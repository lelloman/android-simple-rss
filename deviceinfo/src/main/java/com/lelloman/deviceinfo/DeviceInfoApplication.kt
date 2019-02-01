package com.lelloman.deviceinfo

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.res.Configuration
import com.lelloman.common.di.BaseApplicationModule
import com.lelloman.deviceinfo.di.AppModule
import com.lelloman.deviceinfo.di.DaggerAppComponent
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

open class DeviceInfoApplication : Application(), HasActivityInjector {

    @Inject
    lateinit var dispatchingActivityAndroidInjector: DispatchingAndroidInjector<Activity>

    private val configurationChangesSubject = PublishSubject.create<Any>()

    val configurationChanges: Observable<Any> = configurationChangesSubject.hide()

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        inject()
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        configurationChangesSubject.onNext(Object())
    }

    override fun activityInjector() = dispatchingActivityAndroidInjector

    open fun inject() {
        DaggerAppComponent
            .builder()
            .baseApplicationModule(BaseApplicationModule(this))
            .appModule(AppModule(this))
            .build()
            .inject(this)
    }
}
