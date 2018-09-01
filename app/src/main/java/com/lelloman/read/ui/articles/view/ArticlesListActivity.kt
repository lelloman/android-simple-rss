package com.lelloman.read.ui.articles.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.lelloman.read.R
import com.lelloman.read.core.SemanticTimeProvider
import com.lelloman.read.core.view.BaseActivity
import com.lelloman.read.databinding.ActivityArticlesListBinding
import com.lelloman.read.ui.articles.viewmodel.ArticlesListViewModel
import dagger.android.AndroidInjection
import javax.inject.Inject

class ArticlesListActivity :
    BaseActivity<ArticlesListViewModel, ActivityArticlesListBinding>() {

    private lateinit var adapter: ArticlesAdapter

    @Inject
    lateinit var semanticTimeProvider: SemanticTimeProvider

    override fun getLayoutId() = R.layout.activity_articles_list

    override fun getViewModelClass() = ArticlesListViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)

        adapter = ArticlesAdapter(
            appSettings = appSettings,
            onArticleClickedListener = viewModel::onArticleClicked,
            uiScheduler = uiScheduler,
            lifecycle = lifecycle,
            semanticTimeProvider = semanticTimeProvider
        )

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        binding.viewModel = viewModel

        viewModel.articles.observe(this, adapter)

        binding.swipeRefreshLayout.setOnRefreshListener(viewModel::refresh)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_articles_list, menu)
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
        else -> super.onOptionsItemSelected(item)
    }

    companion object {
        fun start(activity: Activity) {
            activity.startActivity(Intent(activity, ArticlesListActivity::class.java))
        }
    }
}
