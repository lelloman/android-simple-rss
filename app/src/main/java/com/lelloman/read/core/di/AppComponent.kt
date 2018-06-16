package com.lelloman.read.core.di

import com.lelloman.read.ReadApplication
import com.lelloman.read.http.HttpModule
import com.lelloman.read.persistence.PersistenceModule
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    ActivityContributes::class,
    AndroidInjectionModule::class,
    AppModule::class,
    HttpModule::class,
    PersistenceModule::class,
    ViewModelFactoryModule::class,
    ViewModelModule::class])
interface AppComponent {

    fun inject(app: ReadApplication)
}