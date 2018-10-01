package com.lelloman.kotlinnn.dataset

import com.lelloman.kotlinnn.DataSample
import java.util.*

abstract class DataSet(private val random: Random) {

    abstract val samples: Array<DataSample>
    abstract val size: Int
    abstract val inputDimension: Pair<Int, Int>
    abstract val outputDimension: Pair<Int, Int>

    val randomizer by lazy { IntArray(size, { it }) }

    fun shuffle() {
        val indices = MutableList(size, { it })
        (0 until size).forEach {
            randomizer[it] = indices.removeAt(random.nextInt(indices.size))
        }
    }

    fun sameDimensionAs(other: DataSet) = other.inputDimension == this.inputDimension && other.outputDimension == this.outputDimension

    inline fun forEach(action: (dataSample: DataSample) -> Unit) = (0 until size).forEach {
        val index = randomizer[it]
        val sample = samples[index]
        action(sample)
    }
}
