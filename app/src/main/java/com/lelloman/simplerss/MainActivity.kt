package com.lelloman.simplerss

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.navigation.findNavController
import com.lelloman.simplerss.navigation.NavControllerHolder
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var navControllerHolder: NavControllerHolder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navControllerHolder.navController = findNavController(R.id.navHostFragment)

        if (BuildConfig.DEBUG) {
            window.decorView.keepScreenOn = true
        }
    }

    override fun onDestroy() {
        navControllerHolder.navController = null
        super.onDestroy()
    }
}