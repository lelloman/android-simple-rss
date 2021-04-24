package com.lelloman.simplerss.domain_feed

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable

interface FeedSource {

    val id: String

    fun observeItems(): Observable<List<FeedItem>>

    fun refresh(): Completable

    sealed class Operation {

        class Add(val source: FeedSource) : Operation()

        class Remove(val sourceId: String) : Operation()
    }
}