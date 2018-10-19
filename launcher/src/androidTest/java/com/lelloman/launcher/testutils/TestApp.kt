package com.lelloman.launcher.testutils

import com.lelloman.common.di.BaseApplicationModule
import com.lelloman.common.settings.BaseSettingsModule
import com.lelloman.launcher.LauncherApplication
import com.lelloman.launcher.classification.ClassificationModule
import com.lelloman.launcher.di.DaggerAppComponent
import com.lelloman.launcher.di.LauncherApplicationModule
import com.lelloman.launcher.di.ViewModelModule
import com.lelloman.launcher.packages.PackagesModule
import com.lelloman.launcher.persistence.PersistenceModule
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers

class TestApp : LauncherApplication() {

    var baseApplicationModule = BaseApplicationModule(this)
    private var baseSettingsModule = BaseSettingsModule()
    private var launcherApplicationModule = LauncherApplicationModule()
    var classificationModule = ClassificationModule()
    var packagesModule = PackagesModule()
    var persistenceModule = PersistenceModule()
    var viewModelModule = ViewModelModule()

    override fun onCreate() {
        super.onCreate()
        instance = this
        appSettings.reset()

        Completable
            .fromAction(appDatabase::clearAllTables)
            .subscribeOn(Schedulers.io())
            .blockingAwait()
    }

    override fun inject() = DaggerAppComponent
        .builder()
        .baseApplicationModule(baseApplicationModule)
        .baseSettingsModule(baseSettingsModule)
        .launcherApplicationModule(launcherApplicationModule)
        .packagesModule(packagesModule)
        .viewModelModule(viewModelModule)
        .persistenceModule(persistenceModule)
        .classificationModule(classificationModule)
        .build()
        .inject(this)

    companion object {
        lateinit var instance: TestApp
            private set

        fun dependenciesUpdate(action: (TestApp) -> Unit) {
            action.invoke(instance)
            instance.inject()
        }

        fun resetPersistence() {

        }
    }
}