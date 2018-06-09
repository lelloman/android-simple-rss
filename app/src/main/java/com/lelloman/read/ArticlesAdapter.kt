package com.lelloman.read

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.lelloman.read.databinding.ListItemArticleBinding
import com.lelloman.read.model.Article
import com.lelloman.read.viewmodel.ArticleViewModel

class ArticlesAdapter : RecyclerView.Adapter<ArticlesAdapter.ViewHolder>() {

    private val data = mutableListOf<Article>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<ListItemArticleBinding>(
                LayoutInflater.from(parent.context),
                R.layout.list_item_article,
                parent,
                false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(data[position])

    fun add(article: Article) {
        val position = data.size
        data.add(article)
        notifyItemInserted(position)
    }

    class ViewHolder(private val binding: ListItemArticleBinding)
        : RecyclerView.ViewHolder(binding.root) {

        private val viewModel = ArticleViewModel()

        fun bind(article: Article) {
            viewModel.setArticle(article)
            binding.article = viewModel
            binding.executePendingBindings()
        }
    }
}