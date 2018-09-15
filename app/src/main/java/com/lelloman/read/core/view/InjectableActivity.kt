package com.lelloman.read.core.view

import android.arch.lifecycle.ViewModelProvider
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.lelloman.common.di.qualifiers.IoScheduler
import com.lelloman.common.di.qualifiers.UiScheduler
import com.lelloman.read.core.logger.LoggerFactory
import com.lelloman.read.core.navigation.NavigationRouter
import com.lelloman.read.persistence.settings.AppSettings
import dagger.android.AndroidInjection
import io.reactivex.Scheduler
import javax.inject.Inject

abstract class InjectableActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var navigationRouter: NavigationRouter

    @Inject
    lateinit var appSettings: AppSettings

    @Inject
    @field:UiScheduler
    lateinit var uiScheduler: Scheduler

    @Inject
    @field:IoScheduler
    lateinit var ioScheduler: Scheduler

    @Inject
    lateinit var loggerFactory: LoggerFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
    }
}