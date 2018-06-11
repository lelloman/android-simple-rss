package com.lelloman.read.sources.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.lelloman.read.R
import com.lelloman.read.core.BaseActivity
import com.lelloman.read.databinding.ActivityAddSourceBinding
import com.lelloman.read.sources.viewmodel.AddSourceViewModel

class AddSourceActivity
    : BaseActivity<AddSourceViewModel, ActivityAddSourceBinding>() {

    override fun getLayoutId() = R.layout.activity_add_source

    override fun getViewModelClass() = AddSourceViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    companion object {

        fun start(activity: Activity) {
            activity.startActivity(Intent(activity, AddSourceActivity::class.java))
        }
    }
}
