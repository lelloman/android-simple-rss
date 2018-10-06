package com.lelloman.deviceinfo.testutils

import com.lelloman.common.di.BaseApplicationModule
import com.lelloman.common.settings.BaseSettingsModule
import com.lelloman.deviceinfo.DeviceInfoApplication
import com.lelloman.deviceinfo.di.AppModule
import com.lelloman.deviceinfo.di.DaggerAppComponent
import com.lelloman.deviceinfo.di.ViewModelModule
import io.reactivex.Observable

class TestApp : DeviceInfoApplication() {

    private var baseApplicationModule = BaseApplicationModule(this)
    private var baseSettingsModule = BaseSettingsModule()
    var viewModelModule = ViewModelModule()
    private var appModule = AppModule(this)

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    override fun inject() = DaggerAppComponent
        .builder()
        .baseApplicationModule(baseApplicationModule)
        .appModule(appModule)
        .baseSettingsModule(baseSettingsModule)
        .viewModelModule(viewModelModule)
        .build()
        .inject(this)

    companion object {
        private lateinit var instance: TestApp

        val configurationChanges: Observable<Any> get() = instance.configurationChanges

        fun dependenciesUpdate(action: (TestApp) -> Unit) {
            action.invoke(instance)
            instance.inject()
        }
    }
}