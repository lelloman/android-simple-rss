package com.lelloman.read.testutils

import android.widget.ImageView
import com.lelloman.common.di.BaseApplicationModule
import com.lelloman.common.view.PicassoWrap
import com.lelloman.read.ReadApplication
import com.lelloman.read.di.DaggerAppComponent
import com.lelloman.read.di.ViewModelModule
import com.lelloman.read.http.HttpModule
import com.lelloman.read.persistence.db.DbModule
import com.lelloman.read.persistence.settings.SettingsModule

class TestApp : ReadApplication() {

    var baseApplicationModule = BaseApplicationModule(this)
    var viewModelModule = ViewModelModule()
    var dbModule = DbModule()
    var settingsModule = SettingsModule()
    var httpModule = HttpModule()

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