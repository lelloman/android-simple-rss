package com.lelloman.read.testutils

import com.lelloman.read.ReadApplication
import com.lelloman.read.core.di.AppModule
import com.lelloman.read.core.di.DaggerAppComponent
import com.lelloman.read.core.di.ViewModelModule
import com.lelloman.read.http.HttpModule
import com.lelloman.read.persistence.PersistenceModule

class TestApp : ReadApplication() {

    var viewModelModule = ViewModelModule()
        set(value) {
            field = value
            inject()
        }

    var persistenceModule = PersistenceModule()
        set(value) {
            field = value
            inject()
        }

    var httpModule = HttpModule()
        set(value) {
            field = value
            inject()
        }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    override fun inject() = DaggerAppComponent
        .builder()
        .appModule(AppModule(this))
        .viewModelModule(viewModelModule)
        .persistenceModule(persistenceModule)
        .httpModule(httpModule)
        .build()
        .inject(this)

    companion object {
        lateinit var instance: TestApp
            private set
    }
}