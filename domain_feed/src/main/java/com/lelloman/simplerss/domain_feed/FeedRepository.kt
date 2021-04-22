package com.lelloman.simplerss.domain_feed

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable

interface FeedRepository {

    fun observeFeed(): Observable<List<FeedItem>>

    fun refreshFeed(): Completable

    @Suppress("unused") // being an app-singleton will never actually be disposed
    fun dispose()
}