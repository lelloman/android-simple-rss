package com.lelloman.nn.layer

import com.lelloman.nn.activation.LayerActivation

abstract class Layer(
    val outputSeqLength: Int,
    val outputWidth: Int,
    val inputLayer: Layer?,
    val hasBias: Boolean,
    activationFactory: (Int, Int) -> LayerActivation) {

    protected val activation = activationFactory.invoke(outputSeqLength, outputWidth)

    var isTraining = false

    val output: Array<DoubleArray>
        get() = activation.output

    abstract val weightsSize: Int

    abstract fun setWeights(weights: DoubleArray)
    abstract fun initializeWeights()
    abstract fun deltaWeights(delta: DoubleArray)
    abstract fun weightAt(index: Int): Double
    abstract fun copyWeights(): DoubleArray

    fun setActivation(activation: Array<DoubleArray>) {
        System.arraycopy(activation, 0, this.activation.output, 0, activation.size)
    }

    fun isTrainable() = true

    abstract fun computeActivation()
    abstract fun activationDerivative(sequenceIndex: Int, widthIndex: Int): Double
}