package com.lelloman.simplerss.ui.sources.viewmodel

import android.databinding.ObservableField
import com.lelloman.common.utils.model.ByteArrayWithId
import com.lelloman.common.view.ResourceProvider
import com.lelloman.common.view.SemanticTimeProvider
import com.lelloman.common.viewmodel.BaseListItemViewModel
import com.lelloman.simplerss.R

class SourceListItemViewModel(
    private val resourceProvider: ResourceProvider,
    private val semanticTimeProvider: SemanticTimeProvider,
    private val onIsActiveChanged: (Boolean) -> Unit
) : BaseListItemViewModel<Long, com.lelloman.simplerss.persistence.db.model.Source> {

    private lateinit var source: com.lelloman.simplerss.persistence.db.model.Source

    var name = ""
        private set

    var url = ""
        private set

    var hash = 0
        private set

    var lastFetched = ObservableField<String>()

    var isActive = false
        private set

    var favicon: ByteArrayWithId = ByteArrayWithId(null, -1)
        private set

    override fun bind(item: com.lelloman.simplerss.persistence.db.model.Source) {
        this.source = item
        name = item.name
        url = item.url
        hash = item.immutableHashCode
        setLastFetched()
        isActive = item.isActive
        favicon = ByteArrayWithId(item.favicon, item.id)
    }

    private fun setLastFetched() {
        val lastFetchedValue = if (source.lastFetched <= 0L) {
            resourceProvider.getString(R.string.never)
        } else {
            semanticTimeProvider.getTimeDiffString(source.lastFetched)
        }
        lastFetched.set(resourceProvider.getString(R.string.last_refresh, lastFetchedValue))
    }

    fun tick() {
        setLastFetched()
    }

    fun onIsActiveChanged(isActive: Boolean) = onIsActiveChanged.invoke(isActive)
}