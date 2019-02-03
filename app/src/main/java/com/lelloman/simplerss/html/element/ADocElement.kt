package com.lelloman.simplerss.html.element

class ADocElement(
    parent: com.lelloman.simplerss.html.element.DocElement,
    href: String
) : com.lelloman.simplerss.html.element.HrefDocElement(
    type = com.lelloman.simplerss.html.DocElementType.A,
    parent = parent,
    href = href,
    tag = "a"
)