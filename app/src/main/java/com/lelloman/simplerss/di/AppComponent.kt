package com.lelloman.simplerss.di

import com.lelloman.common.di.BaseApplicationModule
import com.lelloman.common.settings.BaseSettingsModule
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    com.lelloman.simplerss.di.AndroidContributes::class,
    AndroidInjectionModule::class,
    BaseApplicationModule::class,
    BaseSettingsModule::class,
    com.lelloman.simplerss.persistence.db.DbModule::class,
    com.lelloman.simplerss.feed.FeedModule::class,
    com.lelloman.simplerss.html.HtmlModule::class,
    com.lelloman.simplerss.http.HttpModule::class,
    com.lelloman.simplerss.persistence.settings.SettingsModule::class,
    com.lelloman.simplerss.di.ViewModelFactoryModule::class,
    com.lelloman.simplerss.di.ViewModelModule::class
])
interface AppComponent {

    fun inject(app: com.lelloman.simplerss.SimpleRssApplication)
}