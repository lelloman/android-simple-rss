package com.lelloman.domain_sources

import io.reactivex.rxjava3.core.Observable

interface SourceOperationsProducer {

    val id: String

    fun produceSourceOperations(): Observable<List<Source.Operation>>
}