package com.lelloman.read.articleslist.view

import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.lelloman.read.R
import com.lelloman.read.persistence.model.Article
import com.lelloman.read.articleslist.viewmodel.ArticleViewModel
import com.lelloman.read.databinding.ListItemArticleBinding
import com.lelloman.read.utils.ModelWithIdListDiffCalculator
import javax.inject.Inject

class ArticlesAdapter @Inject constructor()
    : RecyclerView.Adapter<ArticlesAdapter.ViewHolder>(), Observer<List<Article>> {

    private var data = emptyList<Article>()
    private val listDiffCalculator = ModelWithIdListDiffCalculator()

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

    override fun onChanged(newData: List<Article>?) {
        newData?.let {
            val diff = listDiffCalculator.computeDiff(data, newData)
            this.data = newData
            diff.dispatchUpdatesTo(this)
        }
    }

    class ViewHolder(private val binding: ListItemArticleBinding)
        : RecyclerView.ViewHolder(binding.root) {

        private val viewModel = ArticleViewModel()

        fun bind(article: Article) {
            viewModel.bind(article)
            binding.article = viewModel
            binding.executePendingBindings()
        }
    }

}