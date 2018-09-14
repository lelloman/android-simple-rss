package com.lelloman.read.testutils

import android.widget.ImageView
import com.lelloman.common.view.PicassoWrap
import com.lelloman.read.ReadApplication
import com.lelloman.read.core.di.AppModule
import com.lelloman.read.core.di.DaggerAppComponent
import com.lelloman.read.core.di.ViewModelModule
import com.lelloman.read.http.HttpModule
import com.lelloman.read.persistence.PersistenceModule

class TestApp : ReadApplication() {

    var appModule = AppModule(this)
    var viewModelModule = ViewModelModule()
    var persistenceModule = PersistenceModule()
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
        .appModule(appModule)
        .viewModelModule(viewModelModule)
        .persistenceModule(persistenceModule)
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