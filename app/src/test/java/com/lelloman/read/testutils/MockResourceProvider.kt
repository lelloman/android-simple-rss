package com.lelloman.read.testutils

import com.lelloman.read.core.ResourceProvider

class MockResourceProvider(
    private var stringArrayLength: Int = 10
) : ResourceProvider {

    override fun getString(stringId: Int, vararg args: Any) =
        "$stringId${args.map { ":$it" }}"

    override fun getStringArray(arrayId: Int) = Array(stringArrayLength) { "$it" }
}