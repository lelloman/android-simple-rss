package com.lelloman.kotlinnn

import com.lelloman.kotlinnn.layer.InputLayer
import com.lelloman.kotlinnn.layer.Layer

class Network private constructor(private val layers: Array<Layer>) {

    val size: Int = layers.size
    val output by lazy { layers.last().output }

    private val forwardLayers: Array<Layer> = layers.sliceArray(IntRange(1, layers.size - 1))

    init {
        forwardLayers.forEach {
            it.initializeWeights()
        }
    }

    fun forwardPass(input: Array<DoubleArray>): Array<DoubleArray> {
        layers[0].setActivation(input)

        forwardLayers.forEach { it.computeActivation() }

        return layers.last().output
    }

    fun layerAt(index: Int) = layers[index]

    fun setTraining(isTraining: Boolean) = layers.forEach {
        it.isTraining = isTraining
    }

    class Builder {
        private val layers = mutableListOf<Layer>()

        fun addLayer(layer: Layer) = apply {
            layers.add(layer)
        }

        fun build(): Network {
            if (layers.size < 2) {
                throw IllegalStateException("A network must have at least an input and an output layer")
            }

            if (layers[0] !is InputLayer) {
                throw IllegalStateException("The first layer of the network must be input")
            }

            (1 until layers.size)
                .filter { layers[it] is InputLayer }
                .forEach { throw IllegalStateException("Layer $it is an input layer, only the first layer can be input") }

            // TODO check for circular network


            return Network(layers.toTypedArray())
        }
    }
}