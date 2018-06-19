package com.lelloman.read.ui.sources.viewmodel

import com.lelloman.read.core.TimeDiffCalculator
import com.lelloman.read.persistence.model.Source

class SourceListItemViewModel(
    private val timeDiffCalculator: TimeDiffCalculator
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
        lastFetched = timeDiffCalculator.getSourceLastFetchedString(source)
    }
}