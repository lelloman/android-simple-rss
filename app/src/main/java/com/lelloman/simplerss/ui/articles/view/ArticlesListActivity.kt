package com.lelloman.simplerss.ui.articles.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.lelloman.simplerss.BuildConfig
import com.lelloman.simplerss.R
import com.lelloman.simplerss.databinding.ActivityArticlesListBinding
import com.lelloman.simplerss.persistence.settings.AppSettings
import com.lelloman.simplerss.ui.SimpleRssActivity
import com.lelloman.simplerss.ui.articles.viewmodel.ArticlesListViewModel
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class ArticlesListActivity :
    SimpleRssActivity<ArticlesListViewModel, ActivityArticlesListBinding>() {

    private lateinit var adapter: ArticlesAdapter

    private val appSettings by inject<AppSettings>()

    override val layoutResId = R.layout.activity_articles_list

    override val viewModel by viewModel<ArticlesListViewModel>()

    override fun setViewModel(
        binding: ActivityArticlesListBinding,
        viewModel: ArticlesListViewModel
    ) {
        binding.viewModel = viewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adapter = ArticlesAdapter(
            appSettings = appSettings,
            onArticleClickedListener = viewModel::onArticleClicked,
            lifecycle = lifecycle
        )

        binding.articlesRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.articlesRecyclerView.adapter = adapter

        viewModel.articles.observe(this, adapter)

        binding.swipeRefreshLayout.setOnRefreshListener(viewModel::refresh)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_articles_list, menu)
        if (BuildConfig.DEBUG) {
            menuInflater.inflate(R.menu.activity_articles_list_debug_ext, menu)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_sources -> {
            viewModel.onSourcesClicked()
            true
        }
        R.id.action_refresh -> {
            viewModel.refresh()
            true
        }
        R.id.action_settings -> {
            viewModel.onSettingsClicked()
            true
        }
        R.id.action_discover_sources -> {
            viewModel.onDiscoverSourceClicked()
            true
        }
        R.id.action_debug -> {
            if (BuildConfig.DEBUG) {
                viewModel.onDebugClicked()
            }
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    companion object {
        fun start(activity: Activity) {
            activity.startActivity(Intent(activity, ArticlesListActivity::class.java))
        }
    }
}
