package com.lelloman.launcher.classification

import com.lelloman.common.LLContext
import com.lelloman.launcher.logger.ShouldLogToFile
import com.lelloman.launcher.persistence.ClassifierPersistence
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

class ClassificationTrigger(
    private val trigger: Observable<Long> = Observable.interval(10, TimeUnit.MINUTES),
    private val packageClassifier: PackageClassifier,
    private val llContext: LLContext,
    private val classifierPersistence: ClassifierPersistence
) : ShouldLogToFile {

    fun start() {
        @Suppress("UNUSED_VARIABLE")
        val unused = trigger
            .observeOn(llContext.ioScheduler)
            .subscribe {
                maybeTriggerPackageClassification()
            }
        maybeTriggerPackageClassification()
    }

    private fun maybeTriggerPackageClassification() {
        val timeSinceLastClassification = llContext.nowUtcMs() - classifierPersistence.lastClassificationTimeMs
        if (timeSinceLastClassification > CLASSIFICATION_INTERVAL_MS) {
            packageClassifier.classifyWithNeuralNet()
            classifierPersistence.lastClassificationTimeMs = llContext.nowUtcMs()
        }
    }

    internal companion object {
        const val CLASSIFICATION_INTERVAL_MS = 1000L * 60 * 20
    }
}