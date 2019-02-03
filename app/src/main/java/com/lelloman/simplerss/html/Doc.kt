package com.lelloman.simplerss.html

class Doc(
    val url: String? = null,
    val baseUrl: String? = url
) : com.lelloman.simplerss.html.element.DocElement(
    type = com.lelloman.simplerss.html.DocElementType.ROOT,
    tag = ""
) {
    fun all(): List<com.lelloman.simplerss.html.element.DocElement> = mutableListOf<com.lelloman.simplerss.html.element.DocElement>()
        .apply {
            iterate { add(it) }
        }
}