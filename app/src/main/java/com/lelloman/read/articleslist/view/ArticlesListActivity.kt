package com.lelloman.read.articleslist.view

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.lelloman.read.R
import com.lelloman.read.articleslist.viewmodel.ArticlesListViewModel
import com.lelloman.read.core.ViewModelFactory
import com.lelloman.read.databinding.ActivityArticlesListBinding
import com.lelloman.read.sourceslist.view.SourcesListActivity
import dagger.android.AndroidInjection
import javax.inject.Inject

class ArticlesListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityArticlesListBinding

    @Inject
    lateinit var adapter: ArticlesAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelFactory<ArticlesListViewModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_articles_list)
        binding.setLifecycleOwner(this)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        ViewModelProviders.of(this, viewModelFactory).get(ArticlesListViewModel::class.java).let { viewModel ->
            binding.viewModel = viewModel

            viewModel.articles.observe(this, adapter)

            binding.swipeRefreshLayout.setOnRefreshListener {
                viewModel.refresh()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_articles_list, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_sources -> {
            SourcesListActivity.start(this)
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}
