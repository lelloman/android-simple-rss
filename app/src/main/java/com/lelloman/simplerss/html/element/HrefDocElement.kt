package com.lelloman.simplerss.html.element

import com.lelloman.simplerss.html.DocElementType

abstract class HrefDocElement(
    type: DocElementType,
    parent: DocElement,
    tag: String,
    val href: String
) : DocElement(
    type = type,
    parent = parent,
    tag = tag
)