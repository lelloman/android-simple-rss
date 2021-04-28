package com.lelloman.simplerss.domain_local_sources

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable

interface LocalSourcesRepository {

    fun observeLocalSources(): Observable<List<LocalSource>>

    fun delete(localSourceId: Long): Completable

    fun update(localSource: LocalSource): Completable

    fun add(name: String, url: String): Completable
}