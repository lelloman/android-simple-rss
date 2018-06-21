package com.lelloman.read.ui.articles.viewmodel

import com.lelloman.read.persistence.db.model.Article
import java.text.SimpleDateFormat
import java.util.*


class ArticleListItemViewModel {

    private val detailTimeFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US)

    var title = ""
        private set

    var details = ""
        private set

    var hash = 0
        private set

    var subtitle = ""
        private set

    var imageVisible = false
        private set

    var subtitleVisible = false
        private set

    var imageUrl: String? = null
        private set

    fun bind(article: Article) {
        title = article.title
        details = "${detailTimeFormat.format(article.time)} - ${article.sourceName}"
        hash = article.hashCode()
        subtitle = article.subtitle
        imageVisible = !article.imageUrl.isNullOrBlank()
        subtitleVisible = article.subtitle.isNotEmpty()
        imageUrl = article.imageUrl
    }
}