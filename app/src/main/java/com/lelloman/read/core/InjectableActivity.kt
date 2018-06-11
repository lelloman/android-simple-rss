package com.lelloman.read.core

import android.os.Bundle
import android.os.PersistableBundle
import android.support.annotation.LayoutRes
import android.support.v7.app.AppCompatActivity
import com.lelloman.read.articleslist.viewmodel.ArticlesListViewModel
import com.lelloman.read.core.navigation.NavigationRouter
import dagger.android.AndroidInjection
import javax.inject.Inject

abstract class InjectableActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory<ArticlesListViewModel>

    @Inject
    lateinit var navigationRouter: NavigationRouter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)

    }
}