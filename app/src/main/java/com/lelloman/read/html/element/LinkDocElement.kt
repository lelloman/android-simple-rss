package com.lelloman.read.html.element

import com.lelloman.read.html.DocElementType

class LinkDocElement(
    parent: DocElement,
    val linkType: String,
    href: String
) : HrefDocElement(
    type = DocElementType.LINK,
    parent = parent,
    href = href,
    tag = "link"
)