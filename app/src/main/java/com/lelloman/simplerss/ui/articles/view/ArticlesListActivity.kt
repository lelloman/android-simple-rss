package com.lelloman.simplerss.ui.articles.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.lelloman.common.navigation.DeepLink
import com.lelloman.common.navigation.DeepLinkStartable
import com.lelloman.common.view.BaseActivity
import com.lelloman.simplerss.R
import com.lelloman.simplerss.databinding.ActivityArticlesListBinding
import dagger.android.AndroidInjection
import javax.inject.Inject

class ArticlesListActivity :
    BaseActivity<com.lelloman.simplerss.ui.articles.viewmodel.ArticlesListViewModel, ActivityArticlesListBinding>() {

    private lateinit var adapter: com.lelloman.simplerss.ui.articles.view.ArticlesAdapter

    @Inject
    lateinit var appSettings: com.lelloman.simplerss.persistence.settings.AppSettings

    override val layoutResId = R.layout.activity_articles_list

    override fun getViewModelClass() = com.lelloman.simplerss.ui.articles.viewmodel.ArticlesListViewModel::class.java

    override fun setViewModel(binding: ActivityArticlesListBinding, viewModel: com.lelloman.simplerss.ui.articles.viewmodel.ArticlesListViewModel) {
        binding.viewModel = viewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)

        adapter = com.lelloman.simplerss.ui.articles.view.ArticlesAdapter(
            appSettings = appSettings,
            onArticleClickedListener = viewModel::onArticleClicked,
            uiScheduler = uiScheduler,
            lifecycle = lifecycle,
            semanticTimeProvider = semanticTimeProvider
        )

        binding.articlesRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.articlesRecyclerView.adapter = adapter

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

        var deepLinkStartable = object : DeepLinkStartable {
            override fun start(context: Context, deepLink: DeepLink) {
                val intent = Intent(context, com.lelloman.simplerss.ui.articles.view.ArticlesListActivity::class.java)
                if (context !is Activity) {
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(intent)
            }
        }
            internal set
    }
}
