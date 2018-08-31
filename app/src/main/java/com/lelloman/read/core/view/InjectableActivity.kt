package com.lelloman.read.core.view

import android.arch.lifecycle.ViewModelProvider
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.lelloman.read.core.navigation.NavigationRouter
import com.lelloman.read.persistence.settings.AppSettings
import dagger.android.AndroidInjection
import javax.inject.Inject

abstract class InjectableActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var navigationRouter: NavigationRouter

    @Inject
    lateinit var appSettings: AppSettings

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
    }
}