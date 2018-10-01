package com.lelloman.kotlinnn.layer

import com.lelloman.kotlinnn.activation.Activation
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Test
import java.util.*

class RecurrentLayerTest {

    private val random = Random()

    @Test
    fun `initializes weights`() {
        val inputLayer = InputLayer(2)
        val weightsSize = 2 * (2 + 1) + 2 * 2
        val weightsInitializer = object : WeightsInitializer {
            override fun initialize(weights: DoubleArray) = Arrays.fill(weights, 2.9)
        }
        val rnn = RecurrentLayer(2, inputLayer, weightsInitializer = weightsInitializer)
        assertThat(rnn.weightsSize).isEqualTo(weightsSize)
        rnn.initializeWeights()

        for (i in 0 until weightsSize) {
            assertThat(rnn.weightAt(i)).isEqualTo(2.9)
        }
    }

    @Test
    fun `can set weights only if weights array size is correct`() {
        val inputLayer = InputLayer(3)

        val rnn1 = RecurrentLayer(2, inputLayer, hasBias = true)
        val correctSize1 = 2 * (3 + 1) + 2 * 2

        rnn1.setWeights(DoubleArray(correctSize1))
        assertThatThrownBy { rnn1.setWeights(DoubleArray(correctSize1 - 1)) }
            .isInstanceOf(IllegalArgumentException::class.java)
        assertThatThrownBy { rnn1.setWeights(DoubleArray(correctSize1 + 1)) }
            .isInstanceOf(IllegalArgumentException::class.java)

        val rnn2 = RecurrentLayer(5, inputLayer, hasBias = false)
        val correctSize2 = 5 * (3 + 0) + 5 * 5

        rnn2.setWeights(DoubleArray(correctSize2))
        assertThatThrownBy { rnn2.setWeights(DoubleArray(correctSize2 - 1)) }
            .isInstanceOf(IllegalArgumentException::class.java)
        assertThatThrownBy { rnn2.setWeights(DoubleArray(correctSize2 + 1)) }
            .isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `sets weights`() {
        val inputLayer = InputLayer(2)
        val weightsSize = 2 * (2 + 1) + 2 * 2
        val rnn = RecurrentLayer(2, inputLayer)
        assertThat(rnn.weightsSize).isEqualTo(weightsSize)

        val weights = DoubleArray(weightsSize, { random.nextDouble() })

        rnn.setWeights(weights)

        weights.forEachIndexed { index, weight ->
            assertThat(rnn.weightAt(index)).isEqualTo(weight)
        }
    }

    @Test
    fun `performs delta weights`() {
        val inputLayer = InputLayer(2)
        val weightsSize = 2 * (2 + 1) + 2 * 2
        val rnn = RecurrentLayer(2, inputLayer)
        assertThat(rnn.weightsSize).isEqualTo(weightsSize)

        val weights = DoubleArray(weightsSize, { random.nextDouble() })
        val weightsDelta = DoubleArray(weightsSize, { random.nextDouble() })

        rnn.setWeights(weights)
        rnn.deltaWeights(DoubleArray(weightsSize, { weightsDelta[it] }))

        weights.forEachIndexed { index, weight ->
            assertThat(rnn.weightAt(index)).isEqualTo(weight + weightsDelta[index])
        }
    }

    @Test
    fun `throws exception if returnSequence is false`() {
        val inputLayer = InputLayer(1)
        val rnn = RecurrentLayer(1, inputLayer, returnSequence = false)

        assertThatThrownBy { rnn.computeActivation() }
            .hasMessageContaining("not implemented")
    }

    @Test
    fun `computes activation with 1 unit without bias`() {
        val inputLayer = InputLayer(1)
        val rnn = RecurrentLayer(1, inputLayer, hasBias = false, activation = Activation.IDENTITY)
        val w1 = random.nextDouble()
        val w2 = random.nextDouble()
        rnn.setWeights(doubleArrayOf(w1, w2))

        val input0 = random.nextDouble()
        inputLayer.setActivation(arrayOf(doubleArrayOf(input0)))
        rnn.computeActivation()

        val activation0 = w1 * input0
        assertThat(rnn.output).isEqualTo(arrayOf(doubleArrayOf(activation0)))

        val input1 = random.nextDouble()
        inputLayer.setActivation(arrayOf(doubleArrayOf(input1)))
        rnn.computeActivation()

        val activation1 = input1 * w1 + activation0 * w2
        assertThat(rnn.output).isEqualTo(arrayOf(doubleArrayOf(activation1)))

        val input2 = random.nextDouble()
        inputLayer.setActivation(arrayOf(doubleArrayOf(input2)))
        rnn.computeActivation()

        val activation2 = input2 * w1 + activation1 * w2
        assertThat(rnn.output).isEqualTo(arrayOf(doubleArrayOf(activation2)))
    }

    @Test
    fun `computes activation with 1 unit and bias`() {
        val inputLayer = InputLayer(1)
        val rnn = RecurrentLayer(1, inputLayer, hasBias = true, activation = Activation.IDENTITY)
        val w1 = random.nextDouble()
        val w2 = random.nextDouble()
        val w3 = random.nextDouble()
        rnn.setWeights(doubleArrayOf(w1, w2, w3))

        val input0 = random.nextDouble()
        inputLayer.setActivation(arrayOf(doubleArrayOf(input0)))
        rnn.computeActivation()

        val activation0 = w1 * input0 + w2
        assertThat(rnn.output).isEqualTo(arrayOf(doubleArrayOf(activation0)))

        val input1 = random.nextDouble()
        inputLayer.setActivation(arrayOf(doubleArrayOf(input1)))
        rnn.computeActivation()

        val activation1 = input1 * w1 + w2 + activation0 * w3
        assertThat(rnn.output).isEqualTo(arrayOf(doubleArrayOf(activation1)))

        val input2 = random.nextDouble()
        inputLayer.setActivation(arrayOf(doubleArrayOf(input2)))
        rnn.computeActivation()

        val activation2 = input2 * w1 + w2 + activation1 * w3
        assertThat(rnn.output).isEqualTo(arrayOf(doubleArrayOf(activation2)))
    }


    @Test
    fun `computes activation with 2 units and bias on input with size 2`() {
        val inputLayer = InputLayer(2)
        val activationFun = Activation.LOGISTIC.factory.invoke(1, 2)
        val rnn = RecurrentLayer(2, inputLayer, hasBias = true, activation = Activation.LOGISTIC)
        val weightsSize = 2 * (2 + 1) + 2 * 2
        val weights = DoubleArray(weightsSize, { random.nextDouble() })
        rnn.setWeights(weights)
        val weightsW = DoubleArray(2 * (2 + 1), { weights[it] })
        val weightsU = DoubleArray(2 * 2, { weights[weightsW.size + it] })

        val input0 = doubleArrayOf(random.nextDouble(), random.nextDouble())
        inputLayer.setActivation(arrayOf(input0))
        rnn.computeActivation()

        activationFun.perform(0, doubleArrayOf(
            input0[0] * weightsW[0] + input0[1] * weightsW[1] + weightsW[2],
            input0[0] * weightsW[3] + input0[1] * weightsW[4] + weightsW[5]
        ))
        val activation0 = activationFun.output[0].clone()
        assertThat(rnn.output).isEqualTo(arrayOf(activation0))

        val input1 = doubleArrayOf(random.nextDouble(), random.nextDouble())
        inputLayer.setActivation(arrayOf(input1))
        rnn.computeActivation()

        activationFun.perform(0, doubleArrayOf(
            input1[0] * weightsW[0] + input1[1] * weightsW[1] + weightsW[2] + activation0[0] * weightsU[0] + activation0[1] * weightsU[1],
            input1[0] * weightsW[3] + input1[1] * weightsW[4] + weightsW[5] + activation0[0] * weightsU[2] + activation0[1] * weightsU[3]
        ))
        val activation1 = activationFun.output.clone()
        assertThat(rnn.output).isEqualTo(activation1)

    }
}