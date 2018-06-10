package com.lelloman.read.core.di

import com.lelloman.read.ReadApplication
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidInjectionModule::class,
    AppModule::class,
    ActivityContributes::class,
    PersistenceModule::class,
    ViewModelModule::class])
interface AppComponent {

    fun inject(app: ReadApplication)
}