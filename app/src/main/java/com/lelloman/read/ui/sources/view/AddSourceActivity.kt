package com.lelloman.read.ui.sources.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.lelloman.read.R
import com.lelloman.read.core.view.BaseActivity
import com.lelloman.read.databinding.ActivityAddSourceBinding
import com.lelloman.read.ui.sources.viewmodel.AddSourceViewModel

class AddSourceActivity : BaseActivity<AddSourceViewModel, ActivityAddSourceBinding>() {

    override fun getLayoutId() = R.layout.activity_add_source

    override fun getViewModelClass() = AddSourceViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel
        supportActionBar?.apply {
            setHomeButtonEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_close_white_24dp)
        }
        intent?.getStringExtra(ARG_SOURCE_NAME)?.let {
            viewModel.sourceName.set(it)
            intent.removeExtra(ARG_SOURCE_NAME)
        }
        intent?.getStringExtra(ARG_SOURCE_URL)?.let {
            viewModel.sourceUrl.set(it)
            intent.removeExtra(ARG_SOURCE_URL)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_add_source, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_save -> {
            viewModel.onSaveClicked()
            true
        }
        android.R.id.home -> {
            viewModel.onCloseClicked()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    companion object {

        private const val ARG_SOURCE_NAME = "SourceName"
        private const val ARG_SOURCE_URL = "SourceUrl"

        fun start(activity: Activity) {
            activity.startActivity(Intent(activity, AddSourceActivity::class.java))
        }

        fun startWithPrefill(activity: Activity, sourceName: String, sourceUrl: String) {
            activity.startActivity(
                Intent(activity, AddSourceActivity::class.java)
                    .putExtra(ARG_SOURCE_NAME, sourceName)
                    .putExtra(ARG_SOURCE_URL, sourceUrl)
            )
        }
    }
}
