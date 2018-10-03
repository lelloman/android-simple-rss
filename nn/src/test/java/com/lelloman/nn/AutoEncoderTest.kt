package com.lelloman.nn

import com.lelloman.nn.activation.Activation
import com.lelloman.nn.dataset.DataSet1D
import com.lelloman.nn.layer.DenseLayer
import com.lelloman.nn.layer.GaussianWeightsInitializer
import com.lelloman.nn.layer.InputLayer
import com.lelloman.nn.optimizer.Adagrad
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.util.*

class AutoEncoderTest {

    @Test
    fun `learns frequency with one neuron`() {

        val waveSampleSize = 16
        val random = Random(1)
        val weightsInitializer = GaussianWeightsInitializer(0.0, 0.2, random)

        val input = InputLayer(1, waveSampleSize)
        val encodedLayer = DenseLayer(8, input, activation = Activation.TANH, weightsInitializer = weightsInitializer)

        val output = DenseLayer(waveSampleSize, encodedLayer, activation = Activation.TANH, weightsInitializer = weightsInitializer)

        val network = Network.Builder()
            .addLayer(input)
            .addLayer(encodedLayer)
            .addLayer(output)
            .build()

        val ks = doubleArrayOf(0.1, 0.2, 0.4, 0.8, 1.6)
        val sample = { _: Int ->
            val k = ks[random.nextInt(ks.size)] + random.nextDouble() * 0.05
            val wave = DoubleArray(waveSampleSize, { Math.sin(it * k) })
            wave to wave
        }
        ks.forEach { k ->
            val s = DoubleArray(waveSampleSize, { Math.sin(it * k) })
            println("k $k ${s.joinToString("")}")
        }
        val trainingSet = DataSet1D.Builder(10000)
            .add(sample)
            .build()

        val validationSet = DataSet1D.Builder(100)
            .add(sample)
            .build()

        val epochs = 1000
        var success = false
        val callback = object : Training.PrintEpochCallback() {
            override fun shouldEndTraining(trainingLoss: Double, validationLoss: Double): Boolean {
                success = true
                return validationLoss < 0.0015
            }
        }
        val eta = 0.00001
        val batchSize = 10
        val optimizer = Adagrad(eta)
        val training = Training(network, trainingSet, validationSet, callback, epochs, optimizer = optimizer, batchSize = batchSize)
        training.perform()

        ks.forEach { k ->
            val wave = DoubleArray(waveSampleSize, { Math.sin(it * k) })
            val reconstructed = network.forwardPass(arrayOf(wave))[0]
            val a = Array(wave.size, { "%+.2f".format(wave[it]) })
            val b = Array(wave.size, { "%+.2f".format(reconstructed[it]) })
            println("original: ${a.joinToString(",")}")
            println("network:  ${b.joinToString(",")}")
            println("")
        }
        assertThat(success).isTrue()
    }
}