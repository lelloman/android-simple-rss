package com.lelloman.read.ui.sources.viewmodel

import com.lelloman.read.core.SemanticTimeProvider
import com.lelloman.read.persistence.db.model.Source
import com.lelloman.read.utils.ByteArrayWithId

class SourceListItemViewModel(
    private val semanticTimeProvider: SemanticTimeProvider,
    private val onIsActiveChanged: (Boolean) -> Unit
) {

    var name = ""
        private set

    var url = ""
        private set

    var hash = 0
        private set

    var lastFetched = ""
        private set

    var isActive = false
        private set

    var favicon: ByteArrayWithId = ByteArrayWithId(null, -1)
        private set

    fun bind(source: Source) {
        name = source.name
        url = source.url
        hash = source.immutableHashCode
        lastFetched = semanticTimeProvider.getSourceLastFetchedString(source)
        isActive = source.isActive
        favicon = ByteArrayWithId(source.favicon, source.id)
    }

    fun onIsActiveChanged(isActive: Boolean) = onIsActiveChanged.invoke(isActive)
}