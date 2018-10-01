package com.lelloman.kotlinnn.activation

class LeakyReluActivation(sequenceLength: Int, size: Int) : LayerActivation(sequenceLength, size) {
    override fun func(z: Double) = Math.min(1.0, if (z < 0.0) z * 0.001 else z)
    override fun funcPrime(y: Double) = if (y <= 0.0) 0.001 else 1.0
}