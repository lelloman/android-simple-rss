package com.lelloman.nn.activation

class LogisticActivation(length: Int, size: Int) : LayerActivation(length, size) {
    override fun func(z: Double) = 1.0 / (1.0 + Math.exp(-z))
    override fun funcPrime(y: Double) = y * (1 - y)
}