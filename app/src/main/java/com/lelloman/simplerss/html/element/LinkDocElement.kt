package com.lelloman.simplerss.html.element

import com.lelloman.simplerss.html.DocElementType

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