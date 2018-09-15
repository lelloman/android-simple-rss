package com.lelloman.read.ui.sources.viewmodel

import android.databinding.ObservableField
import com.lelloman.common.utils.ByteArrayWithId
import com.lelloman.common.viewmodel.BaseListItemViewModel
import com.lelloman.read.core.SemanticTimeProvider
import com.lelloman.read.persistence.db.model.Source

class SourceListItemViewModel(
    private val semanticTimeProvider: SemanticTimeProvider,
    private val onIsActiveChanged: (Boolean) -> Unit
) : BaseListItemViewModel<Source> {

    private lateinit var source: Source

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

    override fun bind(item: Source) {
        this.source = item
        name = item.name
        url = item.url
        hash = item.immutableHashCode
        setLastFetched()
        isActive = item.isActive
        favicon = ByteArrayWithId(item.favicon, item.id)
    }

    private fun setLastFetched(){
        lastFetched.set(semanticTimeProvider.getSourceLastFetchedString(source))
    }

    fun tick(){
        setLastFetched()
    }

    fun onIsActiveChanged(isActive: Boolean) = onIsActiveChanged.invoke(isActive)
}