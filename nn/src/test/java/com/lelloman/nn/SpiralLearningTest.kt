package com.lelloman.nn

import com.lelloman.nn.activation.Activation
import com.lelloman.nn.dataset.DataSet1D
import com.lelloman.nn.layer.DenseLayer
import com.lelloman.nn.layer.GaussianWeightsInitializer
import com.lelloman.nn.layer.InputLayer
import com.lelloman.nn.loss.Loss
import com.lelloman.nn.optimizer.Adagrad
import com.lelloman.nn.optimizer.Momentum
import com.lelloman.nn.optimizer.SGD
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import java.util.*

class SpiralLearningTest {

    inner class Callback(private val network: Network, private val folderName: String, private val limit: Double = 0.04)
        : Training.PrintEpochCallback() {
        override fun onEpoch(epoch: Int, trainingLoss: Double, validationLoss: Double, finished: Boolean) {
            super.onEpoch(epoch, trainingLoss, validationLoss, finished)
            if (epoch % 50 == 0) {
                saveNetworkSampling(network, folderName, "epoch_$epoch")
            }
        }

        override fun shouldEndTraining(trainingLoss: Double, validationLoss: Double) = validationLoss < limit
    }

    private val imgSizeI = 512
    private val imgSizeD = imgSizeI.toDouble()

    private lateinit var trainingSet: DataSet1D
    private lateinit var validationSet: DataSet1D

    private var saveImages = false
    private val epochs = 1000
    private val batchSize = 10
    private val random
        get() = Random(12345)

    @Before
    fun setUp() {
        trainingSet = SpiralDataSet.make(5000, random)
        validationSet = SpiralDataSet.make(1000, random)
    }

    private fun saveNetworkSampling(network: Network, dirName: String, fileName: String) {
        if (saveImages.not()) return

        val img = createImage(imgSizeD)
        for (x in 0 until imgSizeI) {
            val xd = x.toDouble() / imgSizeD
            for (y in 0 until imgSizeI) {
                val outSample = network.forwardPass(arrayOf(doubleArrayOf(xd, y / imgSizeD)))[0]

                val r = (255 * Math.max(0.0, Math.min(1.0, outSample[0]))).toInt().shl(16)
                val g = (255 * Math.max(0.0, Math.min(1.0, outSample[1]))).toInt().shl(8)
                val b = (255 * Math.max(0.0, Math.min(1.0, outSample[2]))).toInt()
                val p = 0xff000000 + r + g + b
                img.setRGB(x, y, p.toInt())
            }
        }
        img.save(dirName, fileName)
    }

    private fun DataSet1D.saveImg(folderName: String, fileName: String) {

        val dataSetImg = createImage(imgSizeD)

        samples.forEach { (inSampleArr, outSampleArr) ->
            val inSample = inSampleArr[0]
            val outSample = outSampleArr[0]

            val x = (inSample[0] * imgSizeD).toInt()
            val y = (inSample[1] * imgSizeD).toInt()

            val r = (255 * outSample[0]).toInt().shl(16)
            val g = (255 * outSample[1]).toInt().shl(8)
            val b = (255 * outSample[2]).toInt()
            val p = 0xff000000 + r + g + b
            dataSetImg.setRGB(x, y, p.toInt())
        }

        dataSetImg.save(folderName, fileName)
    }

    private fun makeNetwork(outputLayerActivation: Activation): Network {
        val input = InputLayer(2)
        val hidden1 = DenseLayer(16, input, activation = Activation.LOGISTIC, weightsInitializer = GaussianWeightsInitializer(0.0, 0.2, random = random))
        val hidden2 = DenseLayer(16, hidden1, activation = Activation.LOGISTIC, weightsInitializer = GaussianWeightsInitializer(0.0, 0.2, random = random))
        val output = DenseLayer(3, hidden2, activation = outputLayerActivation, weightsInitializer = GaussianWeightsInitializer(0.0, 0.2, random = random))

        return Network.Builder()
            .addLayer(input)
            .addLayer(hidden1)
            .addLayer(hidden2)
            .addLayer(output)
            .build()
    }

    @Test
    fun `learns spiral branch classification with logistic SGD MSE`() {
        val folderName = "spiral_sgd_mse"

        trainingSet.saveImg(folderName, "dataset")

        val network = makeNetwork(Activation.LOGISTIC)
        val callback = Callback(network, folderName)

        val optimizer = SGD(0.1)

        val training = Training(network, trainingSet, validationSet, callback, epochs, optimizer = optimizer, batchSize = batchSize)

        saveNetworkSampling(network, folderName, "before")
        training.perform()
        saveNetworkSampling(network, folderName, "trained")

        assertThat(training.validationLoss()).isLessThan(0.04)
    }

    @Test
    fun `learns spiral branch classification with logistic Momentum MSE`() {
        val folderName = "spiral_sgd_mse_momentum"

        trainingSet.saveImg(folderName, "dataset")

        val network = makeNetwork(Activation.LOGISTIC)
        val limit = 0.01
        val callback = Callback(network, folderName, limit)

        val optimizer = Momentum(0.01, momentum = 0.9)

        val training = Training(network, trainingSet, validationSet, callback, epochs, optimizer = optimizer, batchSize = batchSize)

        saveNetworkSampling(network, folderName, "before")
        training.perform()
        saveNetworkSampling(network, folderName, "trained")

        assertThat(training.validationLoss()).isLessThan(limit)
    }

    @Test
    fun `learns spiral branch classification with logistic Adagrad MSE`() {
        val folderName = "spiral_sgd_mse"

        trainingSet.saveImg(folderName, "dataset")

        val network = makeNetwork(Activation.LOGISTIC)
        val limit = 0.01
        val callback = Callback(network, folderName, limit)
        val optimizer = Adagrad(0.0001)

        val training = Training(network, trainingSet, validationSet, callback, epochs, optimizer = optimizer, batchSize = batchSize)

        saveNetworkSampling(network, folderName, "before")
        training.perform()
        saveNetworkSampling(network, folderName, "trained")

        assertThat(training.validationLoss()).isLessThan(limit)
    }

    @Test
    fun `learns spiral branch classification with logistic SGD CrossEntropy`() {
        val folderName = "spiral_logistic_sgd_crossentropy"

        trainingSet.saveImg(folderName, "dataset")

        val network = makeNetwork(Activation.LOGISTIC)
        val callback = Callback(network, folderName)

        val optimizer = SGD(0.01)

        val training = Training(network, trainingSet, validationSet, callback, epochs, loss = Loss.CROSS_ENTROPY, optimizer = optimizer, batchSize = batchSize)

        saveNetworkSampling(network, folderName, "before")
        training.perform()
        saveNetworkSampling(network, folderName, "trained")

        assertThat(training.validationLoss()).isLessThan(0.05)
    }

    @Test
    fun `learns spiral branch classification with softmax SGD CrossEntropy`() {
        val folderName = "spiral_softmax_sgd_crossentropy"

        trainingSet.saveImg(folderName, "dataset")

        val network = makeNetwork(Activation.SOFTMAX)
        val callback = Callback(network, folderName)
        val optimizer = SGD(0.04)

        val training = Training(network, trainingSet, validationSet, callback, epochs, loss = Loss.CROSS_ENTROPY, optimizer = optimizer, batchSize = batchSize)

        saveNetworkSampling(network, folderName, "before")
        training.perform()
        saveNetworkSampling(network, folderName, "trained")

        assertThat(training.validationLoss()).isLessThan(0.05)
    }

    @Test
    fun `learns spiral branch classification with softmax Momentum CrossEntropy`() {
        val folderName = "spiral_softmax_sgd_crossentropy_momentum"

        trainingSet.saveImg(folderName, "dataset")

        val network = makeNetwork(Activation.SOFTMAX)
        val limit = 0.04
        val callback = Callback(network, folderName, limit)
        val optimizer = Momentum(0.01, momentum = 0.9)

        val training = Training(network, trainingSet, validationSet, callback, epochs, loss = Loss.CROSS_ENTROPY, optimizer = optimizer, batchSize = batchSize)

        saveNetworkSampling(network, folderName, "before")
        training.perform()
        saveNetworkSampling(network, folderName, "trained")

        assertThat(training.validationLoss()).isLessThan(limit)
    }

    @Test
    fun `learns spiral branch classification with softmax Adagrad CrossEntropy`() {
        val folderName = "spiral_softmax_sgd_crossentropy_momentum"

        trainingSet.saveImg(folderName, "dataset")

        val network = makeNetwork(Activation.SOFTMAX)
        val limit = 0.04
        val callback = Callback(network, folderName, limit)
        val optimizer = Adagrad(0.0001)

        val training = Training(network, trainingSet, validationSet, callback, epochs, loss = Loss.CROSS_ENTROPY, optimizer = optimizer, batchSize = batchSize)

        saveNetworkSampling(network, folderName, "before")
        training.perform()
        saveNetworkSampling(network, folderName, "trained")

        assertThat(training.validationLoss()).isLessThan(limit)
    }
}