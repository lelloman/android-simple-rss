package com.lelloman.nn.activation

class TanhActivation(sequenceLength: Int, size: Int) : LayerActivation(sequenceLength, size) {
    override fun func(z: Double) = Math.tanh(z)
    override fun funcPrime(y: Double) = 1.0 - Math.pow(y, 2.0)
}