package com.lelloman.simplerss.ui.articles.view

import androidx.lifecycle.Lifecycle
import com.lelloman.common.di.qualifiers.UiScheduler
import com.lelloman.common.view.SemanticTimeProvider
import com.lelloman.common.view.adapter.BaseRecyclerViewAdapter
import com.lelloman.simplerss.R
import com.lelloman.simplerss.databinding.ListItemArticleBinding
import com.lelloman.simplerss.persistence.db.model.SourceArticle
import com.lelloman.simplerss.persistence.settings.AppSettings
import com.lelloman.simplerss.ui.articles.viewmodel.ArticleListItemViewModel
import io.reactivex.Scheduler
import org.koin.core.KoinComponent
import org.koin.core.inject

class ArticlesAdapter(
    private val lifecycle: Lifecycle,
    onArticleClickedListener: (SourceArticle) -> Unit,
    private val appSettings: AppSettings
) : BaseRecyclerViewAdapter<Long, SourceArticle, ArticleListItemViewModel, ListItemArticleBinding>(
    onItemClickListener = onArticleClickedListener
), KoinComponent {

    private val uiScheduler: Scheduler by inject(UiScheduler)
    private val semanticTimeProvider: SemanticTimeProvider by inject()

    override val listItemLayoutResId = R.layout.list_item_article

    override fun bindViewModel(
        binding: ListItemArticleBinding,
        viewModel: ArticleListItemViewModel
    ) {
        binding.viewModel = viewModel
    }

    override fun createViewModel(viewHolder: BaseViewHolder<Long, SourceArticle, ArticleListItemViewModel, ListItemArticleBinding>) =
        ArticleListItemViewModel(
            appSettings = appSettings,
            uiScheduler = uiScheduler,
            lifecycle = lifecycle,
            semanticTimeProvider = semanticTimeProvider
        )
}