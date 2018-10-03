package com.lelloman.nn.loss

import com.lelloman.nn.Network
import com.lelloman.nn.dataset.DataSet1D
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class CrossEntropyLossTest {

    private val crossEntropyLoss = CrossEntropyLoss()

    @Test
    fun `reinitializes values on epoch start`() {
        crossEntropyLoss.onEpochStarted(1, 10, 10)

        Assertions.assertThat(crossEntropyLoss.loss).isEqualTo(0.0)
        Assertions.assertThat(crossEntropyLoss.dataSetSize).isEqualTo(10)
        Assertions.assertThat(crossEntropyLoss.gradients).hasSize(1)
        Assertions.assertThat(crossEntropyLoss.gradients[0]).hasSize(10)

        crossEntropyLoss.loss = 10.0
        crossEntropyLoss.onEpochStarted(1, 20, 20)

        Assertions.assertThat(crossEntropyLoss.loss).isEqualTo(0.0)
        Assertions.assertThat(crossEntropyLoss.dataSetSize).isEqualTo(20)
        Assertions.assertThat(crossEntropyLoss.gradients).hasSize(1)
        Assertions.assertThat(crossEntropyLoss.gradients[0]).hasSize(20)
    }

    @Test
    fun `computes loss on single sample`() {
        val activation = arrayOf(doubleArrayOf(0.0, 0.0, 1.0))
        val target = arrayOf(doubleArrayOf(0.0, 0.0, 1.0))
        crossEntropyLoss.onEpochStarted(1, 3, 1)

        crossEntropyLoss.onEpochSample(activation, target)

        assertThat(crossEntropyLoss.loss).isEqualTo(0.0)
    }

    @Test
    fun `accumulates loss and stores gradients for each epoch sample`() {
        val outputSize = 2
        val dataSetSize = 3

        crossEntropyLoss.onEpochStarted(1, outputSize, dataSetSize)

        var gradients = crossEntropyLoss.onEpochSample(arrayOf(doubleArrayOf(0.0, 1.0)), arrayOf(doubleArrayOf(0.0, 1.0)))
        assertThat(crossEntropyLoss.getEpochLoss()).isBetween(-0.000001, 0.000001)
        assertThat(gradients).isEqualTo(arrayOf(doubleArrayOf(-0.0, -0.0)))

        gradients = crossEntropyLoss.onEpochSample(arrayOf(doubleArrayOf(0.0, 1.0)), arrayOf(doubleArrayOf(1.0, 0.0)))
        assertThat(crossEntropyLoss.getEpochLoss()).isBetween(39.911470, 39.911479)
        assertThat(gradients).isEqualTo(arrayOf(doubleArrayOf(9.999999999999999E25, -9.999999999999999E25)))

        gradients = crossEntropyLoss.onEpochSample(arrayOf(doubleArrayOf(0.0, 1.0)), arrayOf(doubleArrayOf(0.0, 1.0)))
        assertThat(crossEntropyLoss.getEpochLoss()).isBetween(39.911470, 39.911479)
        assertThat(gradients).isEqualTo(arrayOf(doubleArrayOf(-0.0, -0.0)))
    }

    @Test
    fun `computes zero loss on entire dataset`() {
        val dataSet = DataSet1D.Builder(3)
            .add { doubleArrayOf(0.0, 0.0, 1.0) to doubleArrayOf(0.0, 0.0, 1.0) }
            .build()

        val network: Network = mock {
            on { forwardPass(any()) }.thenReturn(arrayOf(doubleArrayOf(0.0, 0.0, 1.0)))
            on { output }.thenReturn(arrayOf(DoubleArray(3)))
        }

        val loss = crossEntropyLoss.compute(network, dataSet)
        Assertions.assertThat(loss).isBetween(-0.000001, 0.000001)
    }

    @Test
    fun `computes non zero loss on entire dataset`() {
        val dataSet = DataSet1D.Builder(3)
            .add { doubleArrayOf(0.0, 0.0, 1.0) to doubleArrayOf(0.0, 0.0, 1.0) }
            .build()

        var i = 0
        val values = arrayOf(
            doubleArrayOf(1.0, 0.0, 0.0),
            doubleArrayOf(0.0, 1.0, 0.0),
            doubleArrayOf(0.0, 0.0, 1.0)
        )

        val network: Network = mock {
            on { forwardPass(any()) }.thenAnswer { arrayOf(values[i++]) }
            on { output }.thenReturn(arrayOf(DoubleArray(3)))
        }

        val loss = crossEntropyLoss.compute(network, dataSet)
        Assertions.assertThat(loss).isBetween(79.822949890, 79.822949899)
    }

}