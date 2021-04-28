package com.lelloman.simplerss.ui_source.list

import io.reactivex.rxjava3.core.Observable

interface SourcesInteractor {

    fun observeSources(): Observable<List<Source>>

    fun navigateToAddLocalSource()

    fun navigateToAddRemoteSource()

    fun navigateToDiscover()

    data class Source(
        val id: String,
        val name: String,
        val sourceType: SourceType
    )

    sealed class SourceType(val id: String) {
        object Local : SourceType("Local")
        object Remote : SourceType("Remote")
    }
}