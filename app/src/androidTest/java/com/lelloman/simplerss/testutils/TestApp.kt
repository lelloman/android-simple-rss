package com.lelloman.simplerss.testutils

import android.widget.ImageView
import com.lelloman.common.di.BaseApplicationModule
import com.lelloman.common.view.PicassoWrap
import com.lelloman.simplerss.di.DaggerAppComponent

class TestApp : com.lelloman.simplerss.SimpleRssApplication() {

    var baseApplicationModule = BaseApplicationModule(this)
    var viewModelModule = com.lelloman.simplerss.di.ViewModelModule()
    private var dbModule = com.lelloman.simplerss.persistence.db.DbModule()
    private var settingsModule = com.lelloman.simplerss.persistence.settings.SettingsModule()
    var httpModule = com.lelloman.simplerss.http.HttpModule()

    override var picassoWrap: PicassoWrap = object : PicassoWrap {
        override fun enableImageSourceIndicator() = Unit

        override fun loadUrlIntoImageView(uri: String, view: ImageView, placeHolderId: Int?) =
            view.setImageResource(placeHolderId ?: 0)
    }
        set(value) {}

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    override fun inject() = DaggerAppComponent
        .builder()
        .baseApplicationModule(baseApplicationModule)
        .viewModelModule(viewModelModule)
        .dbModule(dbModule)
        .settingsModule(settingsModule)
        .httpModule(httpModule)
        .build()
        .inject(this)

    companion object {
        private lateinit var instance: TestApp

        fun dependenciesUpdate(action: (TestApp) -> Unit) {
            action.invoke(instance)
            instance.inject()
        }

        fun resetPersistence() {
            instance.appSettings.reset()
            instance.db.clearAllTables()
        }
    }
}