package com.lelloman.read.articleslist.repository

import com.lelloman.read.articleslist.model.Article
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import java.util.*
import java.util.concurrent.TimeUnit

class ArticlesRepository {

    private val loadingSubject = BehaviorSubject.create<Boolean>()

    private val articlesSubject = BehaviorSubject.create<List<Article>>()

    private val random = Random()
    private val randomArticle
        get() = Article(
            id = random.nextLong(),
            title = "article ${Math.abs(random.nextInt())}",
            subtitle = "bla bla bla bla",
            sourceName = "da source",
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
        return Observable.just(listOf(randomArticle, randomArticle, randomArticle))
            .delay(5, TimeUnit.SECONDS)
            .doOnSubscribe { isLoading = true }
            .doOnNext { isLoading = false }
    }
}