package com.lelloman.nn.logicgateslearning

import com.lelloman.nn.Network
import com.lelloman.nn.Training
import com.lelloman.nn.activation.Activation
import com.lelloman.nn.layer.DenseLayer
import com.lelloman.nn.layer.GaussianWeightsInitializer
import com.lelloman.nn.layer.InputLayer
import com.lelloman.nn.layer.WeightsInitializer
import com.lelloman.nn.logicGateDataSet
import java.util.*

abstract class LogicGateTrainingTest {

    abstract fun f(a: Double, b: Double): Double
    abstract val label: String

    protected val lossThreshold = 0.001

    protected val trainingSet by lazy { logicGateDataSet(10000, ::f, Random(1)) }
    protected val validationSet by lazy { logicGateDataSet(100, ::f, Random(1)) }

    protected val callback = object : Training.PrintEpochCallback() {
        override fun shouldEndTraining(trainingLoss: Double, validationLoss: Double) = validationLoss < lossThreshold
    }

    protected val epochs = 1000

    protected val logisticNetwork = makeNetwork(Activation.LOGISTIC, GaussianWeightsInitializer(0.3, 0.3, Random(1)))
    protected val tanhNetwork = makeNetwork(Activation.TANH, GaussianWeightsInitializer(0.3, 0.3, Random(1)))
    protected val reluNetwork = makeNetwork(Activation.RELU, GaussianWeightsInitializer(0.3, 0.2, Random(1)))
    protected val leakyReluNetwork = makeNetwork(Activation.LEAKY_RELU, GaussianWeightsInitializer(0.3, 0.2, Random(1)))

    private fun makeNetwork(activation: Activation, weightsInitializer: WeightsInitializer)
        : Network {
        val inputLayer = InputLayer(2)
        val hiddenLayer = DenseLayer(8, inputLayer, activation = activation, weightsInitializer = weightsInitializer)
        val outputLayer = DenseLayer(1, hiddenLayer, activation = activation, weightsInitializer = weightsInitializer)
        return Network.Builder()
            .addLayer(inputLayer)
            .addLayer(hiddenLayer)
            .addLayer(outputLayer)
            .build()
    }


}