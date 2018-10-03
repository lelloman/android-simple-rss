package com.lelloman.nn

@Suppress("ArrayInDataClass")
data class DataSample(val input: Array<DoubleArray>, val output: Array<DoubleArray>) {

    // TODO maybe check all elements inputWidth?
    val inputShape = input.size to input[0].size
    val outputShape = output.size to output[0].size

    fun sameShapeAs(other: DataSample) = inputShape == other.inputShape && outputShape == other.outputShape
}