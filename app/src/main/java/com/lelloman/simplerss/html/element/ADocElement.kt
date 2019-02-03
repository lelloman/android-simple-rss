package com.lelloman.simplerss.html.element

import com.lelloman.simplerss.html.DocElementType

class ADocElement(
    parent: DocElement,
    href: String
) : HrefDocElement(
    type = DocElementType.A,
    parent = parent,
    href = href,
    tag = "a"
)