package com.lelloman.read.persistence.settings

import io.reactivex.Observable

interface AppSettings {

    val sourceRefreshMinInterval: Observable<SourceRefreshInterval>

    val articleListImagesEnabled: Observable<Boolean>

    val useMeteredNetwork: Observable<Boolean>

    fun setSourceRefreshMinInterval(interval: SourceRefreshInterval)

    fun setArticlesListImagesEnabled(enabled: Boolean)

    fun setUseMeteredNetwork(useMeteredNetwork: Boolean)
}