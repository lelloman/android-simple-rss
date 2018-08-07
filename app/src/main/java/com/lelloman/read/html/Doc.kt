package com.lelloman.read.html

import com.lelloman.read.html.element.DocElement

class Doc(
    val url: String? = null,
    val baseUrl: String? = url
) : DocElement(
    type = DocElementType.ROOT,
    tag = ""
) {
    fun all(): List<DocElement> = mutableListOf<DocElement>()
        .apply {
            iterate { add(it) }
        }
}