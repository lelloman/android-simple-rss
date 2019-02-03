package com.lelloman.simplerss.html.element

class IrrelevantDocElement(
    parent: com.lelloman.simplerss.html.element.DocElement,
    tag: String
) : com.lelloman.simplerss.html.element.DocElement(
    type = com.lelloman.simplerss.html.DocElementType.IRRELEVANT,
    parent = parent,
    tag = tag
)