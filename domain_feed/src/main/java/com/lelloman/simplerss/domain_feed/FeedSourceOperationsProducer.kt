package com.lelloman.simplerss.domain_feed

import io.reactivex.rxjava3.core.Observable

fun interface FeedSourceOperationsProducer {

    fun produceFeedSourceOperations(): Observable<List<FeedSource.Operation>>
}