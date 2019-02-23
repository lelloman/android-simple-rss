package com.lelloman.simplerss.di

import com.lelloman.common.di.BaseApplicationModule
import com.lelloman.common.http.HttpModule
import com.lelloman.common.settings.BaseSettingsModule
import com.lelloman.simplerss.SimpleRssApplication
import com.lelloman.simplerss.feed.FeedModule
import com.lelloman.simplerss.html.HtmlModule
import com.lelloman.simplerss.persistence.db.DbModule
import com.lelloman.simplerss.persistence.settings.SettingsModule
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

    fun inject(app: SimpleRssApplication)
}