package com.lelloman.simplerss.ui.common.repository

import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArticlesRepository @Inject constructor(
    private val articlesDao: com.lelloman.simplerss.persistence.db.ArticlesDao,
    private val feedRefresher: com.lelloman.simplerss.feed.FeedRefresher
) {

    val loading: Observable<Boolean> = feedRefresher
        .isLoading
        .distinctUntilChanged()

    fun fetchArticles(): Observable<List<com.lelloman.simplerss.persistence.db.model.SourceArticle>> = articlesDao
        .getAllFromActiveSources()
        .toObservable()

    fun insertArticles(articles: List<com.lelloman.simplerss.persistence.db.model.Article>): Single<List<Long>> = Single.fromCallable {
        articlesDao.insertAll(*articles.toTypedArray())
    }

    fun refresh() = feedRefresher.refresh()

}