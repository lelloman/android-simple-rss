package com.lelloman.nn

import com.lelloman.nn.dataset.DataSet1D
import com.lelloman.nn.loss.Loss
import com.lelloman.nn.loss.LossFunction
import com.lelloman.nn.optimizer.SGD
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.spy
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Before
import org.junit.Test

class TrainingTestNoSequenceData {

    private val network: Network = mock()

    private val trainingSetSize = 100
    private val validationSetSize = 10
    private val dataDimension = 3

    private val trainingSet = spy(DataSet1D.Builder(trainingSetSize)
        .add { _ -> DoubleArray(dataDimension) to DoubleArray(dataDimension) }
        .build())
    private val validationSet = DataSet1D.Builder(validationSetSize)
        .add { _ -> DoubleArray(dataDimension) to DoubleArray(dataDimension) }
        .build()

    private val callback: Training.EpochCallback = mock()
    private val loss: Loss = mock()
    private val lossFunction: LossFunction = mock()
    private val optimizer: SGD = mock()
    private val epochs = 10
    private val epochsTimes = times(epochs)

    private lateinit var training: Training

    @Before
    fun setUp() {
        whenever(loss.factory).thenReturn { lossFunction }
        whenever(network.output).thenReturn(arrayOf(DoubleArray(dataDimension)))
        whenever(network.forwardPass(any())).thenReturn(arrayOf(DoubleArray(dataDimension)))
        whenever(lossFunction.getEpochLoss()).thenReturn(0.0)
        whenever(lossFunction.onEpochSample(any(), any())).thenReturn(arrayOf(DoubleArray(0)))
    }

    @Test
    fun `performs training with batch size equals to training set`() {
        givenTrainingWithBatchSizeEqualsToTrainingSetSize()

        training.perform()

        verifyTrainingCalls()
        verify(optimizer, times(epochs)).updateWeights()
    }

    @Test
    fun `performs training with batch size 1`() {
        givenTrainingWithBatchSizeEqualsTo(1)

        training.perform()

        verifyTrainingCalls()
        verify(optimizer, times(epochs * trainingSetSize)).updateWeights()
    }

    @Test
    fun `performs training with batch size 5`() {
        givenTrainingWithBatchSizeEqualsTo(5)

        training.perform()

        verifyTrainingCalls()
        verify(optimizer, times(epochs * (trainingSetSize / 5))).updateWeights()
    }

    @Test
    fun `performs training with batch size 7`() {
        givenTrainingWithBatchSizeEqualsTo(7)

        training.perform()

        verifyTrainingCalls()
        verify(optimizer, times(epochs * (trainingSetSize / 7 + 1))).updateWeights()
    }

    private fun verifyTrainingCalls() {
        verify(network, epochsTimes).setTraining(true)
        verify(network, times(epochs * trainingSetSize)).forwardPass(any())
        verify(network, times(1)).setTraining(false)

        verify(callback, epochsTimes).shouldEndTraining(any(), any())
        verify(callback, epochsTimes).onEpoch(any(), any(), any(), any())

        verify(lossFunction, epochsTimes).onEpochStarted(any(), any(), any())
        verify(lossFunction, epochsTimes).compute(network, validationSet)
        verify(lossFunction, epochsTimes).getEpochLoss()

        verify(trainingSet, epochsTimes).shuffle()

        verify(optimizer, times(1)).setup(network)
        verify(optimizer, epochsTimes).onStartEpoch()
        verify(optimizer, times(epochs * trainingSetSize)).trainOnSample(any())
    }

    private fun givenTrainingWithBatchSizeEqualsTo(batchSize: Int) {
        training = Training(network, trainingSet, validationSet, callback, epochs, loss, optimizer, batchSize)
    }

    private fun givenTrainingWithBatchSizeEqualsToTrainingSetSize() = givenTrainingWithBatchSizeEqualsTo(trainingSetSize)

}