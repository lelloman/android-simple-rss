package com.lelloman.kotlinnn.optimizer

import com.lelloman.kotlinnn.Network
import com.lelloman.kotlinnn.layer.DenseLayer
import com.lelloman.kotlinnn.layer.InputLayer
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.reset
import com.nhaarman.mockito_kotlin.spy
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class SGDTest {

    private lateinit var sgd: SGD
    private lateinit var inputLayer: InputLayer
    private lateinit var hiddenLayer: DenseLayer
    private lateinit var outputLayer: DenseLayer
    private lateinit var network: Network

    private val outputLayerWeights = doubleArrayOf(0.1, 0.2, 0.3, 0.4, 0.5, 0.6)
    private val eta = 0.5

    @Before
    fun setUp() {
        inputLayer = spy(InputLayer(2))
        hiddenLayer = spy(DenseLayer(2, inputLayer))
        outputLayer = spy(DenseLayer(2, hiddenLayer))

        network = spy(Network.Builder()
            .addLayer(inputLayer)
            .addLayer(hiddenLayer)
            .addLayer(outputLayer)
            .build())

        outputLayer.setWeights(outputLayerWeights)
        sgd = SGD(eta)
        sgd.setup(network)
        reset(inputLayer)
        reset(hiddenLayer)
        reset(outputLayer)
        reset(network)
    }

    @Test
    fun `when train on sample does not touch input layer`() {
        // initialize lazy fields
        sgd.trainOnSample(arrayOf(doubleArrayOf(0.0, 0.0)))
        reset(network)

        sgd.trainOnSample(arrayOf(doubleArrayOf(0.0, 0.0)))

        verify(network, times(2)).layerAt(2)
        verify(network, times(1)).layerAt(1)
        verify(network, never()).layerAt(0)
    }

    @Test
    fun `updates weights`() {
        // initialize lazy fields
        sgd.updateWeights()
        reset(network.layerAt(0))
        reset(network.layerAt(1))
        reset(network.layerAt(2))
        reset(network)

        sgd.updateWeights()

        verify(network, never()).layerAt(0)
        verify(network).layerAt(1)
        verify(network).layerAt(2)
        verify(network.layerAt(0), never()).deltaWeights(any())
        verify(network.layerAt(1)).deltaWeights(sgd.weightGradients[1])
        verify(network.layerAt(2)).deltaWeights(sgd.weightGradients[2])
    }

    @Test
    fun `computes layers gradients`() {
        val outputError = doubleArrayOf(0.2, 0.4)
        val inputActivation = doubleArrayOf(0.12345, 0.54321)
        val hiddenActivation = doubleArrayOf(0.333, 0.666)
        whenever(inputLayer.output).thenReturn(arrayOf(inputActivation))
        whenever(hiddenLayer.output).thenReturn(arrayOf(hiddenActivation))
        whenever(hiddenLayer.activationDerivative(any(), any())).thenReturn(1.0)
        whenever(outputLayer.activationDerivative(any(), any())).thenReturn(1.0)
        val expectedOutputGradients = doubleArrayOf(
            outputError[0] * hiddenActivation[0] * eta,
            outputError[0] * hiddenActivation[1] * eta,
            outputError[0] * 1 * eta,
            outputError[1] * hiddenActivation[0] * eta,
            outputError[1] * hiddenActivation[1] * eta,
            outputError[1] * 1 * eta
        )
        val expectedHiddenError = doubleArrayOf(
            outputError[0] * outputLayerWeights[0] + outputError[1] * outputLayerWeights[3],
            outputError[0] * outputLayerWeights[1] + outputError[1] * outputLayerWeights[4]
        )
        val expectedHiddenGradients = doubleArrayOf(
            expectedHiddenError[0] * inputActivation[0] * eta,
            expectedHiddenError[0] * inputActivation[1] * eta,
            expectedHiddenError[0] * 1 * eta,
            expectedHiddenError[1] * inputActivation[0] * eta,
            expectedHiddenError[1] * inputActivation[1] * eta,
            expectedHiddenError[1] * 1 * eta
        )

        sgd.trainOnSample(arrayOf(outputError))

        assertThat(sgd.neuronErrors[2]).isEqualTo(outputError)
        assertThat(sgd.weightGradients[2]).isEqualTo(expectedOutputGradients)

        assertThat(sgd.neuronErrors[1]).isEqualTo(expectedHiddenError)
        assertThat(sgd.weightGradients[1]).isEqualTo(expectedHiddenGradients)
    }

}