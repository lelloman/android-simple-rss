package com.lelloman.read.ui.articles.view

import android.arch.lifecycle.Lifecycle
import com.lelloman.common.view.BaseRecyclerViewAdapter
import com.lelloman.read.R
import com.lelloman.read.core.SemanticTimeProvider
import com.lelloman.read.databinding.ListItemArticleBinding
import com.lelloman.read.persistence.db.model.SourceArticle
import com.lelloman.read.persistence.settings.AppSettings
import com.lelloman.read.ui.articles.viewmodel.ArticleListItemViewModel
import io.reactivex.Scheduler

class ArticlesAdapter(
    private val lifecycle: Lifecycle,
    private val uiScheduler: Scheduler,
    onArticleClickedListener: (SourceArticle) -> Unit,
    private val appSettings: AppSettings,
    private val semanticTimeProvider: SemanticTimeProvider
) : BaseRecyclerViewAdapter<SourceArticle, ArticleListItemViewModel, ListItemArticleBinding>(
    onItemClickListener = onArticleClickedListener
) {

    override val listItemLayoutResId = R.layout.list_item_article

    override fun bindViewModel(binding: ListItemArticleBinding, viewModel: ArticleListItemViewModel) {
        binding.viewModel = viewModel
    }

    override fun createViewModel(viewHolder: BaseViewHolder<SourceArticle, ArticleListItemViewModel, ListItemArticleBinding>) =
        ArticleListItemViewModel(
            appSettings = appSettings,
            uiScheduler = uiScheduler,
            lifecycle = lifecycle,
            semanticTimeProvider = semanticTimeProvider
        )
}