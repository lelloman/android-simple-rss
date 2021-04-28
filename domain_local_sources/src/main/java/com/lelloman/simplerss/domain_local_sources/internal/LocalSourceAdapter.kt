package com.lelloman.simplerss.domain_local_sources.internal

import com.lelloman.simplerss.domain_feed.FeedItem
import com.lelloman.simplerss.domain_feed.FeedSource
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

    fun fromLocalSource(localSource: LocalSource, localSourceId: Long = localSource.id): FeedSource {
        return SourceImpl(localSource, localSourceId)
    }

    fun localSourceIdToSourceId(localSourceId: Long) = localSourceId.sourceIdFromLocalSourceId()

    private fun Long.sourceIdFromLocalSourceId() = "LocalSource_${this}"

    private inner class SourceImpl(private val localSource: LocalSource, id: Long) : FeedSource {

        override val id: String = id.sourceIdFromLocalSourceId()

        override val type: FeedSource.Type = LocalSourceType

        override fun observeItems(): Observable<List<FeedItem>> {
            return localSourceItemsDao.observeItemsWithSourceId(localSource.id)
                .map { items -> items.map(::SourceItemImpl) }
        }

        override fun refresh(): Completable = localSourceRefresher.refreshSource(localSource.id)

        private inner class SourceItemImpl(localItem: LocalSourceItemEntity) :
            LocalSourceItem by localItem, FeedItem {

            override val sourceId: String = localSource.id.sourceIdFromLocalSourceId()
            override val feedItemId = sourceId + localItem.id
            override val sourceName = localSource.name
            override val icon = localSource.icon
        }
    }
}