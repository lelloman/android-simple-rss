package com.lelloman.simplerss.html.element

abstract class DocElement(
    val type: com.lelloman.simplerss.html.DocElementType,
    val tag: String,
    val parent: com.lelloman.simplerss.html.element.DocElement? = null
) {
    var children: List<com.lelloman.simplerss.html.element.DocElement> = emptyList()
        internal set

    fun iterate(block: (com.lelloman.simplerss.html.element.DocElement) -> Unit) {
        block.invoke(this)
        children.forEach { it.iterate(block) }
    }
}