package com.lelloman.pdfscores.di

import com.lelloman.common.di.BaseApplicationModule
import com.lelloman.common.settings.BaseSettingsModule
import com.lelloman.pdfscores.PdfScoresApplication
import com.lelloman.pdfscores.persistence.PersistenceModule
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidContributes::class,
    AndroidInjectionModule::class,
    BaseApplicationModule::class,
    BaseSettingsModule::class,
    PersistenceModule::class,
    ViewModelFactoryModule::class,
    ViewModelModule::class
])
interface AppComponent {
    fun inject(app: PdfScoresApplication)
}