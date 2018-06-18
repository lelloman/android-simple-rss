package com.lelloman.read.testutils

import com.lelloman.read.core.ResourceProvider

class MockResourceProvider : ResourceProvider {

    override fun getString(stringId: Int, args: Array<Any>) =
        "$stringId${args.map { ":$it" }}"
}