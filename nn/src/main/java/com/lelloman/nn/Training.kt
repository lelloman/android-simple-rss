package com.lelloman.nn

import com.lelloman.nn.dataset.DataSet
import com.lelloman.nn.loss.Loss
import com.lelloman.nn.optimizer.SGD

class Training(private val network: Network,
               private val trainingSet: DataSet,
               private val validationSet: DataSet,
               private val callback: EpochCallback,
               private val epochs: Int,
               loss: Loss = Loss.MSE,
               private val optimizer: SGD = SGD(),
               private val batchSize: Int = trainingSet.size) {

    interface EpochCallback {
        fun onEpoch(epoch: Int, trainingLoss: Double, validationLoss: Double, finished: Boolean)
        fun shouldEndTraining(trainingLoss: Double, validationLoss: Double) = false
    }

    open class PrintEpochCallback : EpochCallback {
        override fun onEpoch(epoch: Int, trainingLoss: Double, validationLoss: Double, finished: Boolean) {
            println("epoch $epoch training loss $trainingLoss validation loss $validationLoss")
        }
    }

    val loss = loss.factory.invoke()

    init {
        if (trainingSet.sameDimensionAs(validationSet).not()) {
            throw IllegalArgumentException("Training set and validation set must have same dimensions, training " +
                "input/output dimensions are ${trainingSet.inputDimension}/${trainingSet.outputDimension} while " +
                "validation ones are ${validationSet.inputDimension}/${validationSet.outputDimension}")
        }

        optimizer.setup(network)
    }

    fun perform() = (1..epochs).forEach { epoch ->
        network.setTraining(true)

        val trainingLoss = trainEpoch()
        val validationLoss = validationLoss()
        var end = epoch == epochs

        if (callback.shouldEndTraining(trainingLoss, validationLoss)) {
            end = true
        }
        callback.onEpoch(epoch, trainingLoss, validationLoss, end)

        if (end) {
            network.setTraining(false)
            return
        }
    }

    private fun trainEpoch(): Double {

        loss.onEpochStarted(trainingSet.outputDimension.first, trainingSet.outputDimension.second, trainingSet.size)
        trainingSet.shuffle()
        optimizer.onStartEpoch()

        var sampleIndex = 0

        trainingSet.forEach { (input, targetOutput) ->
            val outputActivation = network.forwardPass(input)
            val error = loss.onEpochSample(outputActivation, targetOutput)

            optimizer.trainOnSample(error)
            if (++sampleIndex >= batchSize) {
                optimizer.updateWeights()
                sampleIndex = 0
            }
        }

        if (sampleIndex > 0) {
            optimizer.updateWeights()
        }

        return loss.getEpochLoss()
    }

    fun validationLoss() = loss.compute(network, validationSet)
}