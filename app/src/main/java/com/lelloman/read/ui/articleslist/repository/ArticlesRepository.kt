package com.lelloman.read.ui.articleslist.repository

import com.lelloman.read.core.di.qualifiers.IoScheduler
import com.lelloman.read.persistence.model.Article
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.subjects.BehaviorSubject
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArticlesRepository @Inject constructor(
    @IoScheduler private val ioScheduler: Scheduler
) {

    private val loadingSubject = BehaviorSubject.create<Boolean>()

    private val articlesSubject = BehaviorSubject.create<List<Article>>()

    private val random = Random()
    private val randomArticle
        get() = Article(
            id = random.nextLong(),
            title = "article ${Math.abs(random.nextInt())}",
            subtitle = "bla bla bla bla",
            sourceName = "da source",
            link = "www.staceppa.com",
            content = "si grazie",
            sourceId = 0L,
            time = random.nextLong()
        )

    val loading: Observable<Boolean> = loadingSubject.hide()
        .distinctUntilChanged()

    private var isLoading = false
        set(value) {
            field = value
            loadingSubject.onNext(value)
        }

    fun fetchArticles(): Observable<List<Article>> {
        return articlesSubject.hide()
            .doOnSubscribe { refresh() }
    }

    fun refresh() {
        if (isLoading) return

        isLoading = true

        makeArticleList()
            .subscribeOn(ioScheduler)
            .subscribe {
                articlesSubject.onNext(it)
                isLoading = false
            }
    }

    private fun makeArticleList(): Observable<List<Article>> {
        return Observable.just(listOf(randomArticle, randomArticle, randomArticle))
            .delay(4, TimeUnit.SECONDS)
    }
}