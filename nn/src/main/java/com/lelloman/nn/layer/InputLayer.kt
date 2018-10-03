package com.lelloman.nn.layer

import com.lelloman.nn.activation.InputActivation

class InputLayer(sequenceLength: Int, size: Int)
    : Layer(sequenceLength, size, null, false, { seqLength: Int, size: Int -> InputActivation(seqLength, size) }) {

    constructor(size: Int) : this(1, size)

    override val weightsSize = 0

    override fun setWeights(weights: DoubleArray) {
        throw RuntimeException("Cannot set weights of an InputLayer")
    }

    override fun initializeWeights() {
        throw RuntimeException("Cannot initialize weights of an InputLayer")
    }

    override fun deltaWeights(delta: DoubleArray) {
        throw RuntimeException("Cannot modify weights of an InputLayer")
    }

    override fun weightAt(index: Int): Double {
        throw RuntimeException("Cannot get weights from an InputLayer")
    }

    override fun copyWeights(): DoubleArray {
        throw RuntimeException("Cannot get weights from an InputLayer")
    }

    override fun computeActivation() {
        throw RuntimeException("Cannot compute activation of an InputLayer")
    }

    override fun activationDerivative(sequenceIndex: Int, widthIndex: Int): Double {
        throw RuntimeException("Cannot compute derivative of the activation of an InputLayer")
    }
}