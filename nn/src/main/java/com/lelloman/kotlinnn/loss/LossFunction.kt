package com.lelloman.kotlinnn.loss

import com.lelloman.kotlinnn.Network
import com.lelloman.kotlinnn.dataset.DataSet

interface LossFunction {

    fun onEpochStarted(sequenceLength: Int, outputSize: Int, dataSetSize: Int)

    /**
     * returns the error gradients
     */
    fun onEpochSample(activationSequence: Array<DoubleArray>, targetSequence: Array<DoubleArray>): Array<DoubleArray>

    fun getEpochLoss(): Double
    fun compute(network: Network, dataSet: DataSet): Double
}

enum class Loss(val factory: () -> LossFunction) {
    MSE(::MseLoss),
    CROSS_ENTROPY(::CrossEntropyLoss)
}