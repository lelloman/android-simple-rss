package com.lelloman.kotlinnn.activation

class InputActivation(length: Int, size: Int) : LayerActivation(length, size) {
    override fun func(z: Double) = throw RuntimeException("cannot compute InputActivation")
    override fun funcPrime(y: Double) = throw RuntimeException("cannot differentiate InputActivation")
}