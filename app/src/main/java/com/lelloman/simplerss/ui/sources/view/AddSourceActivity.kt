package com.lelloman.simplerss.ui.sources.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.lelloman.simplerss.R
import com.lelloman.simplerss.databinding.ActivityAddSourceBinding
import com.lelloman.simplerss.ui.SimpleRssActivity
import com.lelloman.simplerss.ui.sources.viewmodel.AddSourceViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class AddSourceActivity : SimpleRssActivity<AddSourceViewModel, ActivityAddSourceBinding>() {

    override val layoutResId = R.layout.activity_add_source

    override val viewModel by viewModel<AddSourceViewModel>()

    override fun setViewModel(binding: ActivityAddSourceBinding, viewModel: AddSourceViewModel) {
        binding.viewModel = viewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

        fun start(activity: Activity, sourceName: String?, sourceUrl: String?) {
            val intent = Intent(activity, AddSourceActivity::class.java)
                .putExtra(ARG_SOURCE_URL, sourceUrl)
                .putExtra(ARG_SOURCE_NAME, sourceName)

            activity.startActivity(intent)
        }
    }
}
