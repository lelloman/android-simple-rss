package com.lelloman.simplerss.domain_feed

import io.reactivex.rxjava3.core.Observable

fun interface FeedSourceOperationProducer {

    fun produceFeedSourceOperations(): Observable<FeedSource.Operation>
}