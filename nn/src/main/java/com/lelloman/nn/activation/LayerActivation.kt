package com.lelloman.nn.activation

abstract class LayerActivation(length: Int, val width: Int) {

    val output = Array(length) { DoubleArray(width) }
    protected val derivatives = Array(length) { DoubleArray(width) }

    open fun perform(index: Int, z: DoubleArray) = output[index].let { output ->
        (0 until width).forEach {
            output[it] = func(z[it])
        }
    }

    open fun performWithDerivative(index: Int, z: DoubleArray) {
        val output = output[index]
        val derivatives = derivatives[index]

        (0 until width).forEach {
            val zi = z[it]
            val v = func(zi)
            output[it] = v
            derivatives[it] = funcPrime(v)
        }
    }

    protected abstract fun func(z: Double): Double

    fun derivative(sequenceIndex: Int, outputIndex: Int) = derivatives[sequenceIndex][outputIndex]

    protected abstract fun funcPrime(y: Double): Double
}
