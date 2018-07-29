package com.lelloman.read.html.element

import com.lelloman.read.html.DocElementType

abstract class DocElement(
    val type: DocElementType,
    val tag: String,
    val parent: DocElement? = null
) {
    var children: List<DocElement> = emptyList()
        internal set
}