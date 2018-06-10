package com.lelloman.read.sourceslist.viewmodel

import com.lelloman.read.sourceslist.model.Source

class SourceViewModel {

    var name: String = ""
        private set

    var url: String = ""
        private set

    var hash: Int = 0
        private set

    fun bind(source: Source) {
        name = source.name
        url = source.url
        hash = source.hashCode()
    }
}