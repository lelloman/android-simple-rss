package com.lelloman.simplerss.html.element

abstract class HrefDocElement(
    type: com.lelloman.simplerss.html.DocElementType,
    parent: com.lelloman.simplerss.html.element.DocElement,
    tag: String,
    val href: String
) : com.lelloman.simplerss.html.element.DocElement(
    type = type,
    parent = parent,
    tag = tag
)