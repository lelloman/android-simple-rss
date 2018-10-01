package com.lelloman.kotlinnn.logicgateslearning

import com.lelloman.kotlinnn.Training
import com.lelloman.kotlinnn.optimizer.SGD
import com.lelloman.kotlinnn.orSample
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class OrTrainingIntegrationTest : LogicGateTrainingTest() {

    override fun f(a: Double, b: Double) = orSample(a, b)

    override val label = "OR"

    @Test
    fun `online learns logic gate with logistic activation multilayer`() {
        println("Training $label logistic activation...")
        val training = Training(logisticNetwork, trainingSet, validationSet, callback, epochs, optimizer = SGD(0.01), batchSize = 10)
        training.perform()

        val loss = training.validationLoss()
        assertThat(loss).isEqualTo(9.392610374129625E-4)
    }

    @Test
    fun `online learns logic gate with tanh activation multilayer`() {
        println("Training $label tanh activation...")
        val training = Training(tanhNetwork, trainingSet, validationSet, callback, epochs, optimizer = SGD(0.01), batchSize = 10)
        training.perform()

        val loss = training.validationLoss()
        assertThat(loss).isEqualTo(6.234707065066278E-4)
    }

    @Test
    fun `online learns logic gate with ReLU activation multilayer`() {
        println("Training $label ReLU activation...")
        val training = Training(reluNetwork, trainingSet, validationSet, callback, epochs, optimizer = SGD(0.01), batchSize = 10)
        training.perform()

        val loss = training.validationLoss()
        assertThat(loss).isEqualTo(1.9324957798787414E-14)
    }

    @Test
    fun `online learns logic gate with leaky ReLU activation multilayer`() {
        println("Training $label leaky ReLU activation...")
        val training = Training(leakyReluNetwork, trainingSet, validationSet, callback, epochs, optimizer = SGD(0.01), batchSize = 10)
        training.perform()

        val loss = training.validationLoss()
        assertThat(loss).isEqualTo(1.9232672763882195E-14)
    }
}