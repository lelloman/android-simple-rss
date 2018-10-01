package com.lelloman.kotlinnn.activation

class IdentityActivation(length: Int, size: Int) : LayerActivation(length, size) {
    override fun func(z: Double) = z
    override fun funcPrime(y: Double) = 0.0
}