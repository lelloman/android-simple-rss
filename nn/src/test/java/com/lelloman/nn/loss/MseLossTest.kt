package com.lelloman.nn.loss

import com.lelloman.nn.Network
import com.lelloman.nn.dataset.DataSet1D
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class MseLossTest {

    private val mseLoss = MseLoss()

    @Test
    fun `reinitializes values on epoch start`() {
        mseLoss.onEpochStarted(1, 10, 10)

        assertThat(mseLoss.loss).isEqualTo(0.0)
        assertThat(mseLoss.dataSetSize).isEqualTo(10)
        assertThat(mseLoss.gradients).hasSize(1)
        assertThat(mseLoss.gradients[0]).hasSize(10)

        mseLoss.loss = 10.0
        mseLoss.onEpochStarted(1, 20, 20)

        assertThat(mseLoss.loss).isEqualTo(0.0)
        assertThat(mseLoss.dataSetSize).isEqualTo(20)
        assertThat(mseLoss.gradients).hasSize(1)
        assertThat(mseLoss.gradients[0]).hasSize(20)
    }

    @Test
    fun `accumulates loss and stores gradients for each epoch sample`() {
        val outputSize = 1
        val dataSetSize = 3

        mseLoss.onEpochStarted(1, outputSize, dataSetSize)

        var gradients = mseLoss.onEpochSample(arrayOf(doubleArrayOf(0.0)), arrayOf(doubleArrayOf(1.0)))
        assertThat(mseLoss.getEpochLoss()).isBetween(0.33333, 0.333334)
        assertThat(gradients).isEqualTo(arrayOf(doubleArrayOf(1.0000)))

        gradients = mseLoss.onEpochSample(arrayOf(doubleArrayOf(0.0)), arrayOf(doubleArrayOf(2.0)))
        assertThat(mseLoss.getEpochLoss()).isBetween(1.66666, 1.666669)
        assertThat(gradients).isEqualTo(arrayOf(doubleArrayOf(2.0000)))

        gradients = mseLoss.onEpochSample(arrayOf(doubleArrayOf(0.0)), arrayOf(doubleArrayOf(3.0)))
        assertThat(mseLoss.getEpochLoss()).isBetween(4.666666, 4.6666667)
        assertThat(gradients).isEqualTo(arrayOf(doubleArrayOf(3.0000)))
    }

    @Test
    fun `computes loss on entire dataset`() {
        val dataSet = DataSet1D.Builder(3)
            .add { doubleArrayOf(0.0) to doubleArrayOf(0.0) }
            .build()

        var i = 0
        val values = Array(3) { doubleArrayOf(it + 1.0) }

        val network: Network = mock {
            on { forwardPass(any()) }.thenAnswer { _ -> arrayOf(values[i++]) }
            on { output }.thenReturn(arrayOf(doubleArrayOf(0.0)))
        }

        val loss = mseLoss.compute(network, dataSet)
        assertThat(loss).isBetween(4.66666, 4.666669)
    }
}