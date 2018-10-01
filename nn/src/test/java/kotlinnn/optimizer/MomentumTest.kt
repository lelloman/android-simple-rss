package com.lelloman.kotlinnn.optimizer

import com.lelloman.kotlinnn.Network
import com.lelloman.kotlinnn.layer.DenseLayer
import com.lelloman.kotlinnn.layer.InputLayer
import com.lelloman.kotlinnn.layer.WeightsInitializer
import com.nhaarman.mockito_kotlin.spy
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import java.util.*

class MomentumTest {

    private lateinit var inputLayer: InputLayer
    private lateinit var hiddenLayer: DenseLayer
    private lateinit var outputLayer: DenseLayer
    private lateinit var network: Network
    private lateinit var momentum: Momentum
    private val momentumValue = 0.99

    @Before
    fun setUp() {
        val weightsInitializer = object : WeightsInitializer {
            override fun initialize(weights: DoubleArray) = Arrays.fill(weights, 0.0)
        }

        inputLayer = InputLayer(2)
        hiddenLayer = DenseLayer(2, inputLayer, weightsInitializer = weightsInitializer)
        outputLayer = DenseLayer(2, hiddenLayer, weightsInitializer = weightsInitializer)

        network = spy(Network.Builder()
            .addLayer(inputLayer)
            .addLayer(hiddenLayer)
            .addLayer(outputLayer)
            .build())

        momentum = Momentum(0.01, momentumValue)
        momentum.setup(network)
    }

    @Test
    fun `adjusts weights gradients with momentum`() {
        momentum.weightGradients.forEach {
            Arrays.fill(it, 0.1)
        }
        momentum.prevWeightGradients.forEach {
            Arrays.fill(it, 0.2)
        }

        momentum.updateWeights()

        val expectedHiddenWeights = DoubleArray(hiddenLayer.weightsSize, { 0.1 + 0.2 * momentumValue })
        val expectedOutputWeights = DoubleArray(outputLayer.weightsSize, { 0.1 + 0.2 * momentumValue })
        assertThat(hiddenLayer.copyWeights()).isEqualTo(expectedHiddenWeights)
        assertThat(outputLayer.copyWeights()).isEqualTo(expectedOutputWeights)
    }
}