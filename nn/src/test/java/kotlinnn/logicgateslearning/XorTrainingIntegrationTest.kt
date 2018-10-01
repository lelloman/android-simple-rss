package com.lelloman.nn.logicgateslearning

import com.lelloman.nn.Training
import com.lelloman.nn.optimizer.SGD
import com.lelloman.nn.xorSample
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class XorTrainingIntegrationTest : LogicGateTrainingTest() {

    override fun f(a: Double, b: Double) = xorSample(a, b)

    override val label = "XOR"

    @Test
    fun `online learns logic gate with logistic activation multilayer`() {
        println("Training $label logistic activation...")
        val training = Training(logisticNetwork, trainingSet, validationSet, callback, epochs, optimizer = SGD(0.01), batchSize = 10)
        training.perform()

        val loss = training.validationLoss()
        assertThat(loss).isEqualTo(9.929069580315402E-4)
    }

    @Test
    fun `online learns logic gate with tanh activation multilayer`() {
        println("Training $label tanh activation...")
        val training = Training(tanhNetwork, trainingSet, validationSet, callback, epochs, optimizer = SGD(0.01), batchSize = 10)
        training.perform()

        val loss = training.validationLoss()
        assertThat(loss).isEqualTo(9.846694554945172E-4)
    }

    @Test
    fun `online learns logic gate with ReLU activation multilayer`() {
        println("Training $label ReLU activation...")
        val training = Training(reluNetwork, trainingSet, validationSet, callback, epochs, optimizer = SGD(0.01), batchSize = 10)
        training.perform()

        val loss = training.validationLoss()
        assertThat(loss).isEqualTo(2.0669713959267434E-6)
    }

    @Test
    fun `online learns logic gate with leaky ReLU activation multilayer`() {
        println("Training $label leaky ReLU activation...")
        val training = Training(leakyReluNetwork, trainingSet, validationSet, callback, epochs, optimizer = SGD(0.01), batchSize = 10)
        training.perform()

        val loss = training.validationLoss()
        assertThat(loss).isEqualTo(2.1091530480090247E-6)
    }
}