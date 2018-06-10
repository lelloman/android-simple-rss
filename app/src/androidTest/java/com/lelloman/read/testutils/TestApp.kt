package com.lelloman.read.testutils

import com.lelloman.read.ReadApplication
import com.lelloman.read.core.di.AppModule
import com.lelloman.read.core.di.DaggerAppComponent
import com.lelloman.read.di.MockPersistenceModule
import com.lelloman.read.di.MockViewModelModule

class TestApp : ReadApplication() {

    val viewModelModule = MockViewModelModule()
    val persistenceModule = MockPersistenceModule()

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    override fun inject() = DaggerAppComponent
        .builder()
        .appModule(AppModule(this))
        .viewModelModule(viewModelModule)
        .persistenceModule(persistenceModule)
        .build()
        .inject(this)

    companion object {
        lateinit var instance: TestApp
            private set
    }
}