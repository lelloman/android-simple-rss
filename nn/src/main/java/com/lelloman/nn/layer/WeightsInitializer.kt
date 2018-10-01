package com.lelloman.nn.layer

import java.util.*

interface WeightsInitializer {
    fun initialize(weights: DoubleArray)
}

class GaussianWeightsInitializer(private val mean: Double = 0.0,
                                 private val deviation: Double = 0.3,
                                 private val random: Random = Random()) : WeightsInitializer {

    override fun initialize(weights: DoubleArray) = (0 until weights.size).forEach {
        weights[it] = mean + random.nextGaussian() * deviation
    }
}