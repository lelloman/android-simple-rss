package com.lelloman.launcher.classification

class PackageLaunchEncoder(
    identifiers: List<String>
) {

    private val encodingMap: Map<String, DoubleArray> // <identifier, one-hot>
    private val decodingMap: Map<DoubleArray, String> // <one-hot, identifier>

    val encodedSize: Int

    init {
        encodingMap = identifiers
            .toSet()
            .let {
                it.mapIndexed { index: Int, identifier: String ->
                    identifier to DoubleArray(it.size) { i -> if (i == index) 1.0 else 0.0 }
                }
            }
            .toMap()
        encodedSize = encodingMap.size

        decodingMap = encodingMap
            .entries
            .map { it.value to it.key }
            .toMap()
    }

    fun encode(identifier: String): DoubleArray {
        return encodingMap[identifier]!!
    }

    fun decode(encoded: DoubleArray) = encoded
        .indexOfFirst { it > 0.99 }
        .let { encodedIndex ->
            decodingMap
                .entries
                .firstOrNull {
                    it.key[encodedIndex] > 0.1
                }
                ?.value
                ?: ""
        }
}