package com.lelloman.domain_sources

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable

interface Source {
    val id: String
    val name: String
    val lastRefresh: Long
    val isActive: Boolean
    val icon: ByteArray?
    val immutableHashCode: Int
    val type: Type

    fun observeItems(): Observable<List<SourceItem>>

    fun refresh(): Completable

    sealed class Operation {

        class Add(val source: Source) : Operation()

        class Update(val source: Source) : Operation()

        class Remove(val sourceId: String) : Operation()
    }

    interface Type {
        val name: String
    }
}