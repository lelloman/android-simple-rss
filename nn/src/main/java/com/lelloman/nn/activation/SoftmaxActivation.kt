package com.lelloman.nn.activation

class SoftmaxActivation(sequenceLength: Int, size: Int) : LayerActivation(sequenceLength, size) {

    private val exp = DoubleArray(size)

    override fun perform(index: Int, z: DoubleArray) {
        var sum = 0.0
        (0 until width).forEach {
            val v = Math.exp(z[it])
            exp[it] = v
            sum += v
        }

        val output = output[index]
        (0 until width).forEach {
            output[it] = exp[it] / sum
        }
    }

    override fun performWithDerivative(index: Int, z: DoubleArray) {
        var sum = 0.0
        (0 until width).forEach {
            val v = Math.exp(z[it])
            exp[it] = v
            sum += v
        }

        val output = output[index]
        val derivatives = derivatives[index]

        (0 until width).forEach {
            val a = exp[it]
            val v = a / sum
            output[it] = v
            derivatives[it] = v * (1 - v)
        }
    }

    override fun func(z: Double) = Math.exp(z)
    override fun funcPrime(y: Double) = y * (1 - y)
}