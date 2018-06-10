package com.lelloman.read

import com.lelloman.read.core.di.AppModule
import com.lelloman.read.core.di.DaggerAppComponent

class TestApp : ReadApplication() {

    val persistenceModule = MockPersistenceModule()

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    override fun inject() = DaggerAppComponent
        .builder()
        .appModule(AppModule(this))
        .persistenceModule(persistenceModule)
        .build()
        .inject(this)

    companion object {
        lateinit var instance: TestApp
            private set
    }
}