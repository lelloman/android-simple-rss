package com.lelloman.read.di

import com.lelloman.read.ReadApplication
import dagger.Component
import dagger.android.AndroidInjectionModule


@Component(modules = [
    AndroidInjectionModule::class,
    AppModule::class,
    ActivityContributes::class])
interface AppComponent {

    fun inject(app: ReadApplication)
}