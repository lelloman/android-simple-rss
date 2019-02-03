package com.lelloman.simplerss.html.element

import com.lelloman.simplerss.html.DocElementType

class IrrelevantDocElement(
    parent: DocElement,
    tag: String
) : DocElement(
    type = DocElementType.IRRELEVANT,
    parent = parent,
    tag = tag
)