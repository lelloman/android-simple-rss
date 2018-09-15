package com.lelloman.read.ui.sources.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.lelloman.common.navigation.DeepLink
import com.lelloman.common.navigation.DeepLinkStartable
import com.lelloman.read.R
import com.lelloman.read.core.navigation.ReadNavigationScreen.Companion.ARG_SOURCE_NAME
import com.lelloman.read.core.navigation.ReadNavigationScreen.Companion.ARG_SOURCE_URL
import com.lelloman.read.core.view.BaseActivity
import com.lelloman.read.databinding.ActivityAddSourceBinding
import com.lelloman.read.ui.sources.viewmodel.AddSourceViewModel

class AddSourceActivity : BaseActivity<AddSourceViewModel, ActivityAddSourceBinding>() {

    override val layoutResId = R.layout.activity_add_source

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

        var deepLinkStartable = object : DeepLinkStartable {
            override fun start(context: Context, deepLink: DeepLink) {
                val intent = Intent(context, AddSourceActivity::class.java)
                if (context !is Activity) {
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }

                deepLink.getString(ARG_SOURCE_URL)?.let {
                    intent.putExtra(ARG_SOURCE_URL, it)
                }
                deepLink.getString(ARG_SOURCE_NAME)?.let {
                    intent.putExtra(ARG_SOURCE_NAME, it)
                }

                context.startActivity(intent)
            }
        }
            internal set
    }
}
