package com.lelloman.read.ui.articles.repository

import com.lelloman.read.feed.FeedRefresher
import com.lelloman.read.persistence.db.ArticlesDao
import com.lelloman.read.persistence.db.model.Article
import com.lelloman.read.persistence.db.model.SourceArticle
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArticlesRepository @Inject constructor(
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