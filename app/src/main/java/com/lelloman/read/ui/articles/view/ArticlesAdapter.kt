package com.lelloman.read.ui.articles.view

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.lelloman.read.R
import com.lelloman.read.core.SemanticTimeProvider
import com.lelloman.read.databinding.ListItemArticleBinding
import com.lelloman.read.persistence.db.model.Article
import com.lelloman.read.persistence.settings.AppSettings
import com.lelloman.read.ui.articles.viewmodel.ArticleListItemViewModel
import com.lelloman.read.utils.ModelWithIdListDiffCalculator
import io.reactivex.Scheduler

class ArticlesAdapter(
    private val lifecycle: Lifecycle,
    private val uiScheduler: Scheduler,
    private val onArticleClickedListener: (Article) -> Unit,
    private val appSettings: AppSettings,
    private val semanticTimeProvider: SemanticTimeProvider
) : RecyclerView.Adapter<ArticlesAdapter.ViewHolder>(), Observer<List<Article>> {

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

    inner class ViewHolder(private val binding: ListItemArticleBinding)
        : RecyclerView.ViewHolder(binding.root) {

        private val viewModel = ArticleListItemViewModel(
            appSettings = appSettings,
            uiScheduler = uiScheduler,
            lifecycle = lifecycle,
            semanticTimeProvider = semanticTimeProvider
        )
        private lateinit var article: Article

        init {
            binding.root.setOnClickListener { onArticleClickedListener.invoke(article) }
        }

        fun bind(article: Article) {
            this.article = article
            viewModel.bind(article)
            binding.viewModel = viewModel
            binding.executePendingBindings()
        }
    }

}