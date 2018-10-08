package com.lelloman.launcher.classification

import com.lelloman.common.utils.TimeProvider
import com.lelloman.nn.Network
import com.lelloman.nn.Training
import com.lelloman.nn.activation.Activation
import com.lelloman.nn.dataset.DataSet
import com.lelloman.nn.layer.DenseLayer
import com.lelloman.nn.layer.GaussianWeightsInitializer
import com.lelloman.nn.layer.InputLayer
import com.lelloman.nn.optimizer.Adagrad


class PackageLaunchClassifier(
    trainingSet: DataSet,
    validationSet: DataSet,
    timeProvider: TimeProvider,
    launchesEncoder: PackageLaunchEncoder
) {

    private val network: Network

    init {
        val inputSize = trainingSet.inputDimension.second
        val inputLayer = InputLayer(inputSize)
        val outputSize = launchesEncoder.encodedSize
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
        network = Network
            .Builder()
            .addLayer(inputLayer)
            .addLayer(hiddenLayer)
            .addLayer(outputLayer)
            .build()

        val optimizer = Adagrad(0.00001)
        val epochs = 1000
        val startTime = timeProvider.nowUtcMs()
        val callback = object : Training.PrintEpochCallback() {
            override fun shouldEndTraining(trainingLoss: Double, validationLoss: Double): Boolean {
                return timeProvider.nowUtcMs() - startTime > 1000L * 60 || validationLoss < 0.5
            }
        }

        val training = Training(
            network = network,
            trainingSet = trainingSet,
            validationSet = validationSet,
            callback = callback,
            epochs = epochs,
            optimizer = optimizer
        )
        training.perform()
    }

    fun classify(input: DoubleArray): DoubleArray {
        return network.forwardPass(arrayOf(input))[0]
    }
}
