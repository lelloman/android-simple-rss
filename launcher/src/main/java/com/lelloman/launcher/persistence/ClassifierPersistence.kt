package com.lelloman.launcher.persistence

import com.lelloman.nn.Network

interface ClassifierPersistence {

    var lastClassificationTimeMs: Long

    fun saveClassifier(network: Network)

    fun loadClassifier(): Network?
}