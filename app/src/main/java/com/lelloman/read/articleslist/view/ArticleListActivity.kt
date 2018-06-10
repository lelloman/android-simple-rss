package com.lelloman.read.articleslist.view

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.lelloman.read.R
import com.lelloman.read.articleslist.viewmodel.ArticlesListViewModel
import com.lelloman.read.databinding.ActivityArticlesListBinding
import com.lelloman.read.core.ViewModelFactory
import dagger.android.AndroidInjection
import javax.inject.Inject

class ArticleListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityArticlesListBinding
    private val adapter = ArticlesAdapter()

    @Inject
    lateinit var viewModelFactory: ViewModelFactory<ArticlesListViewModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_articles_list)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        val viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(ArticlesListViewModel::class.java)

        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)

        viewModel.articles.observe(this, adapter)

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.refresh()
        }
    }
}
