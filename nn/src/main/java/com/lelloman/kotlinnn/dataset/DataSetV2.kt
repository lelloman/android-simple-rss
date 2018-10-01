package com.lelloman.kotlinnn.dataset

import com.lelloman.kotlinnn.DataSample
import java.util.*


class DataSetV2(override val samples: Array<DataSample>,
                random: Random)
    : DataSet(random) {

    override val inputDimension = samples[0].inputShape
    override val outputDimension = samples[0].outputShape
    override val size = samples.size

    init {

    }

    class Builder(private val size: Int) {
        private val samples = mutableListOf<DataSample>()
        private var random = Random()

        fun add(action: (index: Int) -> DataSample) = apply {
            (0 until size).forEach {
                samples.add(action(it))
            }
        }

        fun random(random: Random) = apply {
            this.random = random
        }

        fun build() = DataSetV2(samples.toTypedArray(), random)
    }
}
