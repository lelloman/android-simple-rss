package com.lelloman.launcher

import android.app.Activity
import com.lelloman.common.BaseApplication
import com.lelloman.common.di.BaseApplicationModule
import com.lelloman.launcher.di.DaggerAppComponent
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

open class LauncherApplication : BaseApplication(), HasActivityInjector {

    @Inject
    lateinit var dispatchingActivityAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun activityInjector() = dispatchingActivityAndroidInjector

    override fun inject() = DaggerAppComponent
        .builder()
        .baseApplicationModule(BaseApplicationModule(this))
        .build()
        .inject(this)
}