package com.lelloman.nn.loss

import com.lelloman.nn.Network
import com.lelloman.nn.dataset.DataSet

internal class CrossEntropyLoss : LossFunction {

    internal var loss = 0.0
    internal var dataSetSize = 0
    internal lateinit var gradients: Array<DoubleArray>

    override fun onEpochStarted(sequenceLength: Int, outputSize: Int, dataSetSize: Int) {
        this.dataSetSize = dataSetSize
        gradients = Array(sequenceLength, { DoubleArray(outputSize) })
        loss = 0.0
    }

    override fun onEpochSample(activationSequence: Array<DoubleArray>, targetSequence: Array<DoubleArray>): Array<DoubleArray> {
        (0 until activationSequence.size).forEach { sequenceIndex ->
            val activation = activationSequence[sequenceIndex]
            val target = targetSequence[sequenceIndex]
            val gradients = gradients[sequenceIndex]
            loss += activation.mapIndexed { index, y ->
                val t = target[index]
                val oneMinusY = 1 - y

                gradients[index] = -(y - t) / ((y * oneMinusY) + EPSILON)
                -t * Math.log(y + EPSILON) - (1 - t) * Math.log(oneMinusY + EPSILON)
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

    companion object {
        private const val EPSILON = 0.00000000000000000000000001
    }
}