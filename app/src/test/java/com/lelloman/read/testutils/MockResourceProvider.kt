package com.lelloman.read.testutils

import com.lelloman.read.core.ResourceProvider

class MockResourceProvider(
    private var defaultStringArrayLength: Int = 10
) : ResourceProvider {

    private val arraysMap: MutableMap<Int, Array<String>> = hashMapOf()

    override fun getString(stringId: Int, vararg args: Any) =
        "$stringId${args.joinToString(separator = "") { ":$it" }}"

    override fun getStringArray(arrayId: Int) = if (arraysMap.containsKey(arrayId)) {
        arraysMap[arrayId]!!
    } else {
        Array(defaultStringArrayLength) { "$it" }
    }

    fun registerStringArray(arrayId: Int, array: Array<String>) {
        arraysMap[arrayId] = array
    }
}