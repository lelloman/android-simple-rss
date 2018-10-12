package com.lelloman.launcher.classification

import com.lelloman.common.LLContext
import com.lelloman.nn.Network
import com.lelloman.nn.Training
import com.lelloman.nn.activation.Activation
import com.lelloman.nn.dataset.DataSet
import com.lelloman.nn.layer.DenseLayer
import com.lelloman.nn.layer.GaussianWeightsInitializer
import com.lelloman.nn.layer.InputLayer
import com.lelloman.nn.loss.Loss
import com.lelloman.nn.optimizer.Adagrad

class NnFactory(
    private val llContext: LLContext
) {

    fun makeClassifier(
        inputSize: Int,
        outputSize: Int
    ): Network {
        val inputLayer = InputLayer(inputSize)
        val hiddenSize = (inputSize + outputSize) / 2
        val hiddenLayer = DenseLayer(
            size = hiddenSize,
            inputLayer = inputLayer,
            activation = Activation.LEAKY_RELU,
            weightsInitializer = GaussianWeightsInitializer()
        )
        val outputLayer = DenseLayer(
            size = outputSize,
            inputLayer = hiddenLayer,
            activation = Activation.SOFTMAX,
            weightsInitializer = GaussianWeightsInitializer()
        )
        return Network
            .Builder()
            .addLayer(inputLayer)
            .addLayer(hiddenLayer)
            .addLayer(outputLayer)
            .build()
    }

    fun makeTraining(
        network: Network,
        trainingSet: DataSet,
        validationSet: DataSet
    ): Training {

        val optimizer = Adagrad(0.00001)
        val epochs = 1000
        val startTime = llContext.nowUtcMs()
        val callback = object : Training.PrintEpochCallback() {
            override fun shouldEndTraining(trainingLoss: Double, validationLoss: Double): Boolean {
                return llContext.nowUtcMs() - startTime > 1000L * 60 * 2 || validationLoss < 0.5
            }
        }
        val batchSize = 10

        return Training(
            network = network,
            trainingSet = trainingSet,
            validationSet = validationSet,
            callback = callback,
            epochs = epochs,
            batchSize = batchSize,
            optimizer = optimizer,
            loss = LOSS
        )
    }

    companion object {
        val LOSS = Loss.MSE
    }
}