package com.lelloman.read.html.element

import com.lelloman.read.html.DocElementType

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