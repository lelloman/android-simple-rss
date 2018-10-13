package com.lelloman.launcher.testutils

import com.lelloman.common.di.BaseApplicationModule
import com.lelloman.common.settings.BaseSettingsModule
import com.lelloman.launcher.LauncherApplication
import com.lelloman.launcher.di.DaggerAppComponent
import com.lelloman.launcher.di.LauncherApplicationModule
import com.lelloman.launcher.di.ViewModelModule
import com.lelloman.launcher.packages.PackagesModule
import com.lelloman.launcher.persistence.PersistenceModule

class TestApp : LauncherApplication() {

    private var baseApplicationModule = BaseApplicationModule(this)
    private var baseSettingsModule = BaseSettingsModule()
    private var launcherApplicationModule = LauncherApplicationModule()
    private var packagesModule = PackagesModule()
    var persistenceModule = PersistenceModule()
    var viewModelModule = ViewModelModule()

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    override fun inject() = DaggerAppComponent
        .builder()
        .baseApplicationModule(baseApplicationModule)
        .baseSettingsModule(baseSettingsModule)
        .launcherApplicationModule(launcherApplicationModule)
        .packagesModule(packagesModule)
        .viewModelModule(viewModelModule)
        .persistenceModule(persistenceModule)
        .build()
        .inject(this)

    companion object {
        private lateinit var instance: TestApp

        fun dependenciesUpdate(action: (TestApp) -> Unit) {
            action.invoke(instance)
            instance.inject()
        }

        fun resetPersistence() {

        }
    }
}