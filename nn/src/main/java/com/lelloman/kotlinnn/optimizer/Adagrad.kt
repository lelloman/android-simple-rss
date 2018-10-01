package com.lelloman.kotlinnn.optimizer

import java.util.*

class Adagrad(eta: Double = 0.01) : SGD(eta) {

    private val prevGradientsSquaredSum by lazy {
        Array(network.size, { DoubleArray(weightGradients[it].size) })
    }

    override fun updateWeights() = (1 until network.size).forEach {

        val gradients = weightGradients[it]
        val prevSquaredSum = prevGradientsSquaredSum[it]

        gradients.forEachIndexed { index, gradient ->
            val prevSum = prevSquaredSum[index]
            gradients[index] = gradient / Math.sqrt(prevSum + e)
            prevSquaredSum[index] = prevSum + Math.pow(gradient, 2.0)
        }

        network.layerAt(it).deltaWeights(gradients)

        Arrays.fill(weightGradients[it], 0.0)
    }

    companion object {
        private const val e = 0.00000001
    }
}