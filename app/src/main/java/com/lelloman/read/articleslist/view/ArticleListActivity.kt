package com.lelloman.read.articleslist.view

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.lelloman.read.R
import com.lelloman.read.articleslist.viewmodel.ArticlesListViewModel
import com.lelloman.read.databinding.ActivityArticlesListBinding

class ArticleListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityArticlesListBinding
    private val adapter = ArticlesAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_articles_list)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        val viewModel = ViewModelProviders.of(this)
            .get(ArticlesListViewModel::class.java)

        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)

        viewModel.articles.observe(this, adapter)

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.refresh()
        }
    }
}
