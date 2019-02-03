package com.lelloman.simplerss.html

import com.lelloman.simplerss.html.element.DocElement

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