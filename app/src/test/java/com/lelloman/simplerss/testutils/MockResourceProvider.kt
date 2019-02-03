package com.lelloman.simplerss.testutils

import android.graphics.drawable.Drawable
import com.lelloman.common.view.ResourceProvider

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

    override fun getDrawable(drawableId: Int): Drawable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}