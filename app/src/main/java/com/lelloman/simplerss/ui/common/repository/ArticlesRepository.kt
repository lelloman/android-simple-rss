package com.lelloman.simplerss.ui.common.repository

import com.lelloman.simplerss.feed.FeedRefresher
import com.lelloman.simplerss.persistence.db.ArticlesDao
import com.lelloman.simplerss.persistence.db.model.Article
import com.lelloman.simplerss.persistence.db.model.SourceArticle
import io.reactivex.Observable
import io.reactivex.Single

class ArticlesRepository(
    private val articlesDao: ArticlesDao,
    private val feedRefresher: FeedRefresher
) {

    val loading: Observable<Boolean> = feedRefresher
        .isLoading
        .distinctUntilChanged()

    fun fetchArticles(): Observable<List<SourceArticle>> = articlesDao
        .getAllFromActiveSources()
        .toObservable()

    fun insertArticles(articles: List<Article>): Single<List<Long>> = Single.fromCallable {
        articlesDao.insertAll(*articles.toTypedArray())
    }

    fun refresh() = feedRefresher.refresh()

}