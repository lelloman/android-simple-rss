package com.lelloman.read.di

import com.lelloman.common.di.BaseApplicationModule
import com.lelloman.common.settings.BaseSettingsModule
import com.lelloman.read.ReadApplication
import com.lelloman.read.feed.FeedModule
import com.lelloman.read.html.HtmlModule
import com.lelloman.read.http.HttpModule
import com.lelloman.read.persistence.db.DbModule
import com.lelloman.read.persistence.settings.SettingsModule
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidContributes::class,
    AndroidInjectionModule::class,
    BaseApplicationModule::class,
    BaseSettingsModule::class,
    DbModule::class,
    FeedModule::class,
    HtmlModule::class,
    HttpModule::class,
    SettingsModule::class,
    ViewModelFactoryModule::class,
    ViewModelModule::class
])
interface AppComponent {

    fun inject(app: ReadApplication)
}