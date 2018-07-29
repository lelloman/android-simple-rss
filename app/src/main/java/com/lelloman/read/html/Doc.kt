package com.lelloman.read.html

import com.lelloman.read.html.element.DocElement

class Doc : DocElement(
    type = DocElementType.ROOT,
    tag = ""
) {
    fun all(): List<DocElement> = mutableListOf<DocElement>()
        .apply {
            iterate { add(it) }
        }
}