package com.lelloman.read.ui.articleslist.repository

import com.lelloman.read.core.di.qualifiers.IoScheduler
import com.lelloman.read.feed.FeedManager
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
    private val feedManager: FeedManager
) {

    val loading: Observable<Boolean> = feedManager.isLoading

    fun fetchArticles(): Observable<List<Article>> = articlesDao
        .getAll()
        .toObservable()


    fun refresh() = feedManager.refresh()

}