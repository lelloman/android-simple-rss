package com.lelloman.kotlinnn.layer

import com.lelloman.kotlinnn.activation.Activation

open class DenseLayer(size: Int,
                      inputLayer: Layer,
                      hasBias: Boolean = true,
                      activation: Activation = Activation.LOGISTIC,
                      private val weightsInitializer: WeightsInitializer = GaussianWeightsInitializer(0.0, 0.3))
    : Layer(1, size, inputLayer, hasBias, activation.factory) {

    private val z = DoubleArray(size)

    override val weightsSize: Int by lazy { weights.size }
    private val weights: DoubleArray = DoubleArray(size * inputLayer!!.outputWidth + (if (hasBias) size else 0), { 0.0 })

    override fun setWeights(weights: DoubleArray) {
        if (weights.size != this.weightsSize) {
            throw IllegalArgumentException("Weights inputWidth is supposed to be $weightsSize for this layer but" +
                "argument has inputWidth ${weights.size}")
        }

        System.arraycopy(weights, 0, this.weights, 0, weights.size)
    }

    override fun initializeWeights() = weightsInitializer.initialize(this.weights)

    override fun deltaWeights(delta: DoubleArray) {
        if (weightsSize != delta.size) {
            throw IllegalArgumentException("Weight updates inputWidth is supposed to be $weightsSize for this layer but" +
                "argument has inputWidth ${delta.size}")
        }

        delta.forEachIndexed { index, d -> weights[index] += d }
    }

    override fun weightAt(index: Int) = weights[index]

    override fun copyWeights() = weights.clone()

    override fun computeActivation() {
        val input = inputLayer!!.output[0]
        val inputSize = input.size

        var weightOffset = 0

        for (i in 0 until outputWidth) {
            var v = (0 until inputSize).sumByDouble { input[it] * weights[weightOffset++] }
            if (hasBias) {
                v += weights[weightOffset++]
            }
            z[i] = v
        }

        if (isTraining) {
            activation.performWithDerivative(0, z)
        } else {
            activation.perform(0, z)
        }
    }

    override fun activationDerivative(sequenceIndex: Int, index: Int) = activation.derivative(sequenceIndex, index)
}