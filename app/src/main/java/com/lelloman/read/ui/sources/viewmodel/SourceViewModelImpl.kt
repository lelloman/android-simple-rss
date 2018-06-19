package com.lelloman.read.ui.sources.viewmodel

import com.lelloman.read.core.ResourceProvider
import com.lelloman.read.ui.sources.repository.SourcesRepository

class SourceViewModelImpl(
    resourceProvider: ResourceProvider,
    sourcesRepository: SourcesRepository
) : SourceViewModel(
    resourceProvider = resourceProvider,
    sourcesRepository = sourcesRepository
) {

}