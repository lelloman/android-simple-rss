package com.lelloman.read.sources.view

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.lelloman.read.R

class AddSourceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_source)
    }

    companion object {

        fun start(activity: Activity){
            activity.startActivity(Intent(activity, AddSourceActivity::class.java))
        }
    }
}
