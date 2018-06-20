package com.lelloman.read.ui.articles.repository

import com.lelloman.read.core.di.qualifiers.IoScheduler
import com.lelloman.read.feed.FeedRefresher
import com.lelloman.read.persistence.db.ArticlesDao
import com.lelloman.read.persistence.db.model.Article
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArticlesRepository @Inject constructor(
    @IoScheduler private val ioScheduler: Scheduler,
    private val articlesDao: ArticlesDao,
    private val feedRefresher: FeedRefresher
) {

    val loading: Observable<Boolean> = feedRefresher.isLoading

    fun fetchArticles(): Observable<List<Article>> = articlesDao
        .getAllFromActiveSources()
        .toObservable()


    fun refresh() = feedRefresher.refresh()

}