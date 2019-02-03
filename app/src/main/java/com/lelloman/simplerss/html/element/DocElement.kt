package com.lelloman.simplerss.html.element

import com.lelloman.simplerss.html.DocElementType

abstract class DocElement(
    val type: DocElementType,
    val tag: String,
    val parent: DocElement? = null
) {
    var children: List<DocElement> = emptyList()
        internal set

    fun iterate(block: (DocElement) -> Unit) {
        block.invoke(this)
        children.forEach { it.iterate(block) }
    }
}