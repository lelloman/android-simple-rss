package com.lelloman.simplerss.domain_local_sources.internal

import com.lelloman.domain_sources.Source
import com.lelloman.domain_sources.SourceItem
import com.lelloman.simplerss.domain_local_sources.LocalSource
import com.lelloman.simplerss.domain_local_sources.LocalSourceItem
import com.lelloman.simplerss.domain_local_sources.internal.db.LocalSourceItemEntity
import com.lelloman.simplerss.domain_local_sources.internal.db.LocalSourceItemsDao
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable

internal class LocalSourceAdapter(
    private val localSourceRefresher: LocalSourceRefresher,
    private val localSourceItemsDao: LocalSourceItemsDao
) {

    fun fromLocalSource(localSource: LocalSource, localSourceId: Long = localSource.id): Source {
        return SourceImpl(localSource, localSourceId)
    }

    fun localSourceIdToSourceId(localSourceId: Long) = localSourceId.sourceIdFromLocalSourceId()

    private fun Long.sourceIdFromLocalSourceId() = "LocalSource_${this}"

    private inner class SourceImpl(private val localSource: LocalSource, id: Long) : Source {

        override val id: String = id.sourceIdFromLocalSourceId()

        override val name: String = localSource.name
        override val lastRefresh: Long = localSource.lastRefresh
        override val isActive: Boolean = localSource.isActive
        override val icon: ByteArray? = localSource.icon
        override val immutableHashCode: Int = localSource.immutableHashCode

        override val type: Source.Type = LocalSourceType

        override fun observeItems(): Observable<List<SourceItem>> {
            return localSourceItemsDao.observeItemsWithSourceId(localSource.id)
                .map { items -> items.map(::SourceItemImpl) }
        }

        override fun refresh(): Completable = localSourceRefresher.refreshSource(localSource.id)

        private inner class SourceItemImpl(localItem: LocalSourceItemEntity) :
            LocalSourceItem by localItem, SourceItem {

            override val sourceId: String = localSource.id.sourceIdFromLocalSourceId()
            override val sourceItemId = sourceId + localItem.id
            override val sourceName = localSource.name
            override val icon = localSource.icon
        }
    }
}