package com.lelloman.nn.loss

import com.lelloman.nn.Network
import com.lelloman.nn.dataset.DataSet

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