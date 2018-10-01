package com.lelloman.nn.dataset

import com.lelloman.nn.DataSample
import java.util.*


class DataSet1D(input: Array<DoubleArray>,
                output: Array<DoubleArray>,
                random: Random)
    : DataSet(random) {

    override val inputDimension: Pair<Int, Int>
    override val outputDimension: Pair<Int, Int>
    override val size = input.size

    override val samples: Array<DataSample>

    init {
        if (input.size != output.size) {
            throw IllegalArgumentException("DataSet input and output must have equal inputWidth, input has ${input.size} samples while output has ${output.size}")
        }

        if (input.isEmpty()) {
            throw IllegalArgumentException("DataSet must have data in it, input and output are empty")
        }

        val inputSize = input[0].size
        input.forEachIndexed { index, value ->
            if (inputSize != value.size) {
                throw IllegalArgumentException("All input samples must have the same dimension, input sample at index 0 has inputWidth $inputSize while input sample at index $index has ${value.size}")
            }
        }

        val outputSize = output[0].size
        output.forEachIndexed { index, value ->
            if (outputSize != value.size) {
                throw IllegalArgumentException("All output samples must have the same dimension, output sample at index 0 has inputWidth $outputSize while output sample at index $index has ${value.size}")
            }
        }

        inputDimension = 1 to inputSize
        outputDimension = 1 to outputSize

        samples = Array(size, { DataSample(arrayOf(input[it]), arrayOf(output[it])) })
    }

    class Builder(private val size: Int) {
        private val input = mutableListOf<DoubleArray>()
        private val output = mutableListOf<DoubleArray>()
        private var random = Random()

        fun add(action: (index: Int) -> Pair<DoubleArray, DoubleArray>) = apply {
            (0 until size).forEach {
                val sample = action(it)
                input.add(sample.first)
                output.add(sample.second)
            }
        }

        fun random(random: Random) = apply {
            this.random = random
        }

        fun build() = DataSet1D(input.toTypedArray(), output.toTypedArray(), random)
    }
}