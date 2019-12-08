package com.lelloman.simplerss.testutils

import android.widget.ImageView
import com.lelloman.common.di.BaseApplicationModuleFactory
import com.lelloman.common.http.HttpModuleFactory
import com.lelloman.common.view.PicassoWrap
import com.lelloman.simplerss.SimpleRssApplication
import com.lelloman.simplerss.di.DbModuleFactory
import com.lelloman.simplerss.di.SettingsModuleFactory
import com.lelloman.simplerss.di.ViewModelModuleFactory
import com.lelloman.simplerss.persistence.db.AppDatabase
import org.koin.android.ext.android.inject
import org.koin.core.context.loadKoinModules

class TestApp : SimpleRssApplication() {

    var baseApplicationModuleFactory = BaseApplicationModuleFactory()
    var viewModelModuleFactory = ViewModelModuleFactory()
    private var dbModuleFactory = DbModuleFactory()
    private var settingsModuleFactory = SettingsModuleFactory()
    var httpModuleFactory = HttpModuleFactory()

    private val db by inject<AppDatabase>()

    override var picassoWrap: PicassoWrap = object : PicassoWrap {
        override fun enableImageSourceIndicator() = Unit

        override fun loadUrlIntoImageView(uri: String, view: ImageView, placeHolderId: Int?) =
            view.setImageResource(placeHolderId ?: 0)
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    override fun inject() {
        super.inject()
        loadKoinModules(
            listOf(
                baseApplicationModuleFactory.makeKoinModule(true),
                viewModelModuleFactory.makeKoinModule(true),
                dbModuleFactory.makeKoinModule(true),
                settingsModuleFactory.makeKoinModule(true),
                httpModuleFactory.makeKoinModule(true)
            )
        )
    }

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