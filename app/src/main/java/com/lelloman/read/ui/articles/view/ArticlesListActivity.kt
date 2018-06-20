package com.lelloman.read.ui.articles.view

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.lelloman.read.R
import com.lelloman.read.core.view.BaseActivity
import com.lelloman.read.databinding.ActivityArticlesListBinding
import com.lelloman.read.ui.articles.viewmodel.ArticlesListViewModel
import dagger.android.AndroidInjection

class ArticlesListActivity :
    BaseActivity<ArticlesListViewModel, ActivityArticlesListBinding>() {

    private lateinit var adapter: ArticlesAdapter

    override fun getLayoutId() = R.layout.activity_articles_list

    override fun getViewModelClass() = ArticlesListViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)

        adapter = ArticlesAdapter(viewModel::onArticleClicked)

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
        else -> super.onOptionsItemSelected(item)
    }
}
