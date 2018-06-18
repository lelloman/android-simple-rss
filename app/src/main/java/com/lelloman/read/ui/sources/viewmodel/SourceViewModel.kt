package com.lelloman.read.ui.sources.viewmodel

import com.lelloman.read.R
import com.lelloman.read.core.ResourceProvider
import com.lelloman.read.core.TimeDiffCalculator
import com.lelloman.read.persistence.model.Source

class SourceViewModel(
    private val timeDiffCalculator: TimeDiffCalculator,
    private val resourceProvider: ResourceProvider
) {

    var name = ""
        private set

    var url = ""
        private set

    var hash = 0
        private set

    var lastFetched = ""
        private set

    fun bind(source: Source) {
        name = source.name
        url = source.url
        hash = source.immutableHashCode

        val lastFetchedValue = if (source.lastFetched <= 0L) {
            resourceProvider.getString(R.string.never)
        } else {
            timeDiffCalculator.getTimeDiffString(source.lastFetched)
        }

        lastFetched = resourceProvider.getString(R.string.last_refresh, lastFetchedValue)
    }
}