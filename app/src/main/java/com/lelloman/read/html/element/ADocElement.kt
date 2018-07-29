package com.lelloman.read.html.element

import com.lelloman.read.html.DocElementType

class ADocElement(
    parent: DocElement,
    href: String
) : HrefDocElement(
    type = DocElementType.A,
    parent = parent,
    href = href,
    tag = "a"
)