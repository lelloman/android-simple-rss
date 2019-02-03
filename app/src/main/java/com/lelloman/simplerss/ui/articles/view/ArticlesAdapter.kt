package com.lelloman.simplerss.ui.articles.view

import android.arch.lifecycle.Lifecycle
import com.lelloman.common.view.SemanticTimeProvider
import com.lelloman.common.view.adapter.BaseRecyclerViewAdapter
import com.lelloman.simplerss.R
import com.lelloman.simplerss.databinding.ListItemArticleBinding
import io.reactivex.Scheduler

class ArticlesAdapter(
    private val lifecycle: Lifecycle,
    private val uiScheduler: Scheduler,
    onArticleClickedListener: (com.lelloman.simplerss.persistence.db.model.SourceArticle) -> Unit,
    private val appSettings: com.lelloman.simplerss.persistence.settings.AppSettings,
    private val semanticTimeProvider: SemanticTimeProvider
) : BaseRecyclerViewAdapter<Long, com.lelloman.simplerss.persistence.db.model.SourceArticle, com.lelloman.simplerss.ui.articles.viewmodel.ArticleListItemViewModel, ListItemArticleBinding>(
    onItemClickListener = onArticleClickedListener
) {

    override val listItemLayoutResId = R.layout.list_item_article

    override fun bindViewModel(binding: ListItemArticleBinding, viewModel: com.lelloman.simplerss.ui.articles.viewmodel.ArticleListItemViewModel) {
        binding.viewModel = viewModel
    }

    override fun createViewModel(viewHolder: BaseViewHolder<Long, com.lelloman.simplerss.persistence.db.model.SourceArticle, com.lelloman.simplerss.ui.articles.viewmodel.ArticleListItemViewModel, ListItemArticleBinding>) =
        com.lelloman.simplerss.ui.articles.viewmodel.ArticleListItemViewModel(
            appSettings = appSettings,
            uiScheduler = uiScheduler,
            lifecycle = lifecycle,
            semanticTimeProvider = semanticTimeProvider
        )
}