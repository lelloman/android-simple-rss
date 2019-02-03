package com.lelloman.simplerss.html.element

class LinkDocElement(
    parent: com.lelloman.simplerss.html.element.DocElement,
    val linkType: String,
    href: String
) : com.lelloman.simplerss.html.element.HrefDocElement(
    type = com.lelloman.simplerss.html.DocElementType.LINK,
    parent = parent,
    href = href,
    tag = "link"
)