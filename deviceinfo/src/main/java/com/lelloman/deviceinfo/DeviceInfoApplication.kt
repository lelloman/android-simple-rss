package com.lelloman.deviceinfo

import android.app.Activity
import android.content.res.Configuration
import com.lelloman.common.BaseApplication
import com.lelloman.common.di.BaseApplicationModule
import com.lelloman.deviceinfo.di.AppModule
import com.lelloman.deviceinfo.di.DaggerAppComponent
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

open class DeviceInfoApplication : BaseApplication(), HasActivityInjector {

    @Inject
    lateinit var dispatchingActivityAndroidInjector: DispatchingAndroidInjector<Activity>

    private val configurationChangesSubject = PublishSubject.create<Any>()

    val configurationChanges = configurationChangesSubject.hide()

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        configurationChangesSubject.onNext(Object())
    }

    override fun activityInjector() = dispatchingActivityAndroidInjector

    override fun inject() {
        DaggerAppComponent
            .builder()
            .baseApplicationModule(BaseApplicationModule(this))
            .appModule(AppModule(this))
            .build()
            .inject(this)
    }
}
