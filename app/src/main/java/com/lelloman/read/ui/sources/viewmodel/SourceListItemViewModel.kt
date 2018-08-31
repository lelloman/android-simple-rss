package com.lelloman.read.ui.sources.viewmodel

import android.databinding.ObservableField
import com.lelloman.read.core.SemanticTimeProvider
import com.lelloman.read.persistence.db.model.Source
import com.lelloman.read.utils.ByteArrayWithId

class SourceListItemViewModel(
    private val semanticTimeProvider: SemanticTimeProvider,
    private val onIsActiveChanged: (Boolean) -> Unit
) {

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

    fun bind(source: Source) {
        this.source = source
        name = source.name
        url = source.url
        hash = source.immutableHashCode
        setLastFetched()
        isActive = source.isActive
        favicon = ByteArrayWithId(source.favicon, source.id)
    }

    private fun setLastFetched(){
        lastFetched.set(semanticTimeProvider.getSourceLastFetchedString(source))
    }

    fun tick(){
        setLastFetched()
    }

    fun onIsActiveChanged(isActive: Boolean) = onIsActiveChanged.invoke(isActive)
}