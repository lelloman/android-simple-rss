package com.lelloman.kotlinnn.layer

import com.lelloman.kotlinnn.activation.Activation

class RecurrentLayer(
    sequenceLength: Int,
    size: Int,
    inputLayer: Layer,
    private val returnSequence: Boolean = true,
    hasBias: Boolean = true,
    activation: Activation = Activation.LOGISTIC,
    private val weightsInitializer: WeightsInitializer = GaussianWeightsInitializer(0.0, 0.3))
    : Layer(sequenceLength, size, inputLayer, hasBias, activation.factory) {

    constructor(size: Int,
                inputLayer: Layer,
                returnSequence: Boolean = true,
                hasBias: Boolean = true,
                activation: Activation = Activation.LOGISTIC,
                weightsInitializer: WeightsInitializer = GaussianWeightsInitializer(0.0, 0.3))
        : this(1, size, inputLayer, returnSequence, hasBias, activation, weightsInitializer)

    private val z = DoubleArray(size)
    private val prevActivation = DoubleArray(size)

    private val weightsW: DoubleArray = DoubleArray(size * inputLayer!!.outputWidth + (if (hasBias) size else 0), { 0.0 })
    private val weightsU: DoubleArray = DoubleArray(size * size, { 0.0 })

    override val weightsSize: Int = weightsW.size + weightsU.size

    override fun setWeights(weights: DoubleArray) {
        if (weights.size != this.weightsSize) {
            throw IllegalArgumentException("Weights inputWidth is supposed to be ${this.weightsSize} for this layer but" +
                "argument has inputWidth ${weights.size}")
        }

        System.arraycopy(weights, 0, this.weightsW, 0, weightsW.size)
        System.arraycopy(weights, weightsW.size, this.weightsU, 0, weightsU.size)
    }

    override fun initializeWeights() {
        weightsInitializer.initialize(weightsW)
        weightsInitializer.initialize(weightsU)
    }

    override fun deltaWeights(delta: DoubleArray) {
        if (weightsSize != delta.size) {
            throw IllegalArgumentException("Weight updates inputWidth is supposed to be $weightsSize for this layer but" +
                "argument has inputWidth ${delta.size}")
        }

        (0 until weightsW.size).forEach { weightsW[it] += delta[it] }
        (0 until weightsU.size).forEach { weightsU[it] += delta[it + weightsW.size] }
    }

    override fun weightAt(index: Int) = if (index < weightsW.size) {
        weightsW[index]
    } else {
        weightsU[index - weightsW.size]
    }

    override fun copyWeights(): DoubleArray {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun computeActivation() {
        if (!returnSequence) TODO("RecurrentLayer not returning sequences is not implemented yet")

        val input = inputLayer!!.output[0]
        val inputSize = input.size

        var weightOffsetW = 0

        for (i in 0 until outputWidth) {
            var v = (0 until inputSize).sumByDouble { input[it] * weightsW[weightOffsetW++] }
            if (hasBias) {
                v += weightsW[weightOffsetW++]
            }
            z[i] = v
        }

        var weightOffsetU = 0
        for (i in 0 until outputWidth) {
            z[i] += (0 until outputWidth).sumByDouble { prevActivation[it] * weightsU[weightOffsetU++] }
        }

        if (isTraining) {
            activation.performWithDerivative(0, z)
        } else {
            activation.perform(0, z)
        }

        System.arraycopy(output[0], 0, prevActivation, 0, outputWidth)
    }

    override fun activationDerivative(sequenceIndex: Int, index: Int) = activation.derivative(sequenceIndex, index)
}