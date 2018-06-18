package com.lelloman.read.ui.articleslist.repository

import com.lelloman.read.core.di.qualifiers.IoScheduler
import com.lelloman.read.feed.FeedRefresher
import com.lelloman.read.persistence.ArticlesDao
import com.lelloman.read.persistence.model.Article
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
        .getAll()
        .toObservable()


    fun refresh() = feedRefresher.refresh()

}