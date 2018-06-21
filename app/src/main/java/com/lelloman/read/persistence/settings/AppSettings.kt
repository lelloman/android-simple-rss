package com.lelloman.read.persistence.settings

interface AppSettings {

    var sourceRefreshMinInterval: SourceRefreshInterval

    var articleListImages: Boolean

    var useMeteredNetwork: Boolean
}