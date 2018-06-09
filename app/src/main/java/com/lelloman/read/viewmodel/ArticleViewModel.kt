package com.lelloman.read.viewmodel

import com.lelloman.read.model.Article
import java.text.SimpleDateFormat
import java.util.*

private val detailTimeFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US)

class ArticleViewModel {

    var title = ""
        private set

    var details = ""
        private set

    var hash = 0
        private set

    var subtitle = ""
        private set

    fun setArticle(article: Article) {
        title = article.title
        details = "${detailTimeFormat.format(article.time)} - ${article.sourceName}"
        hash = article.hashCode()
        subtitle = article.subtitle
    }
}