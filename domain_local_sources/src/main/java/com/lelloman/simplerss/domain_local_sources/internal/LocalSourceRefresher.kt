package com.lelloman.simplerss.domain_local_sources.internal

import com.lelloman.simplerss.domain_local_sources.internal.db.LocalSourcesDao
import io.reactivex.rxjava3.core.Completable
import java.util.concurrent.TimeUnit

internal class LocalSourceRefresher(
    private val localSourcesDao: LocalSourcesDao
) {

    fun refreshSource(localSourceId: Long): Completable {
        return Completable.timer(2, TimeUnit.SECONDS)
    }
}