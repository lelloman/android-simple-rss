package com.lelloman.simplerss.domain_remote_sources

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable

interface RemoteSourcesRepository {

    fun observeRemoteSources(): Observable<List<RemoteSource>>

    fun delete(remoteSourceId: Long): Completable

    fun update(remoteSource: RemoteSource): Completable

    fun add(remoteSource: RemoteSource): Completable
}