package com.lelloman.kotlinnn.loss

import com.lelloman.kotlinnn.Network
import com.lelloman.kotlinnn.dataset.DataSet

internal class MseLoss : LossFunction {

    internal var loss = 0.0
    internal var dataSetSize = 0
    internal lateinit var gradients: Array<DoubleArray>

    override fun onEpochStarted(sequenceLength: Int, outputSize: Int, dataSetSize: Int) {
        this.dataSetSize = dataSetSize
        gradients = Array(sequenceLength, { DoubleArray(outputSize) })
        loss = 0.0
    }

    override fun onEpochSample(activationSequence: Array<DoubleArray>, targetSequence: Array<DoubleArray>): Array<DoubleArray> {
        if (activationSequence.size != 1 || targetSequence.size != 1) {
            throw RuntimeException("Sequence learning not supported")
        }
        (0 until activationSequence.size).forEach { sequenceIndex ->
            val activation = activationSequence[sequenceIndex]
            val target = targetSequence[sequenceIndex]
            val gradients = gradients[sequenceIndex]
            loss += activation.mapIndexed { index, v ->
                val diff = target[index] - v
                gradients[index] = diff
                val e = Math.pow(diff, 2.0)
                e
            }.sum() / dataSetSize
        }
        return gradients
    }

    override fun getEpochLoss(): Double = loss

    override fun compute(network: Network, dataSet: DataSet): Double {
        onEpochStarted(dataSet.outputDimension.first, dataSet.outputDimension.second, dataSet.size)
        dataSet.samples.map { (inSample, outSample) ->
            onEpochSample(network.forwardPass(inSample), outSample)
        }
        return loss
    }
}