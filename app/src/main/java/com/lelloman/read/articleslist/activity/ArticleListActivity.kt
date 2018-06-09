package com.lelloman.read.articleslist.activity

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.lelloman.read.R
import com.lelloman.read.articleslist.adapter.ArticlesAdapter
import com.lelloman.read.articleslist.viewmodel.ArticlesListViewModel

class ArticleListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adapter = ArticlesAdapter()
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        val viewModel = ViewModelProviders.of(this)
                .get(ArticlesListViewModel::class.java)

        viewModel.articles.observe(this, adapter)

    }
}
