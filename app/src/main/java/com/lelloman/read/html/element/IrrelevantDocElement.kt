package com.lelloman.read.html.element

import com.lelloman.read.html.DocElementType

class IrrelevantDocElement(
    parent: DocElement,
    tag: String
) : DocElement(
    type = DocElementType.IRRELEVANT,
    parent = parent,
    tag = tag
)