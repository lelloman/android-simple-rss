package com.lelloman.simplerss.sources

import com.lelloman.simplerss.domain_local_sources.LocalSourcesRepository
import com.lelloman.simplerss.ui_source.local.LocalSourceInteractor

class LocalSourceInteractorImpl(
    private val localSourcesRepository: LocalSourcesRepository
) : LocalSourceInteractor {

    override fun addLocalSource(name: String, url: String) = localSourcesRepository.add(name = name, url = url)
}