package com.lelloman.launcher.classification

import com.lelloman.launcher.persistence.model.PackageLaunch

class PackageLaunchEncoder(
    launches: List<PackageLaunch>
) {

    private val encodingMap: Map<String, DoubleArray> // <identifier, one-hot>
    private val decodingMap: Map<DoubleArray, String> // <one-hot, identifier>

    init {
        encodingMap = launches
            .asSequence()
            .map { it.identifier() }
            .toSet()
            .let {
                it.mapIndexed { index: Int, identifier: String ->
                    identifier to DoubleArray(it.size) { i -> if (i == index) 1.0 else 0.0 }
                }
            }
            .toMap()
        decodingMap = encodingMap
            .entries
            .map { it.value to it.key }
            .toMap()
    }

    fun encode(launch: PackageLaunch): DoubleArray {
        return encodingMap[launch.identifier()]!!
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