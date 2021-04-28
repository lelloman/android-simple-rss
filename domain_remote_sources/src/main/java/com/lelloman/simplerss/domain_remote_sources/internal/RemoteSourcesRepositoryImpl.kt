package com.lelloman.simplerss.domain_remote_sources.internal

import com.lelloman.simplerss.domain_remote_sources.RemoteSource
import com.lelloman.simplerss.domain_remote_sources.RemoteSourcesRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable

internal class RemoteSourcesRepositoryImpl : RemoteSourcesRepository {

    override fun observeRemoteSources(): Observable<List<RemoteSource>> {
        return Observable.empty()
    }

    override fun delete(remoteSourceId: Long): Completable {
        TODO("Not yet implemented")
    }

    override fun update(remoteSource: RemoteSource): Completable {
        TODO("Not yet implemented")
    }

    override fun add(remoteSource: RemoteSource): Completable {
        TODO("Not yet implemented")
    }
}