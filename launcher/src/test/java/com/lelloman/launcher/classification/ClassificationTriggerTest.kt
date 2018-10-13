package com.lelloman.launcher.classification

import com.lelloman.common.LLContext
import com.lelloman.common.utils.TimeProvider
import com.lelloman.launcher.classification.ClassificationTrigger.Companion.CLASSIFICATION_INTERVAL_MS
import com.lelloman.launcher.persistence.ClassifierPersistence
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers.trampoline
import io.reactivex.subjects.PublishSubject
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class ClassificationTriggerTest {

    private val intervalSubject = PublishSubject.create<Long>().doOnSubscribe {
        intervalSubscriptions += 1
    }

    private var intervalSubscriptions = 0
    private val intervalObservable: Observable<Long> = intervalSubject.hide()

    private val packageClassifier: PackageClassifier = mock()
    private val timeProvider: TimeProvider = mock()
    private val classifierPersistence: ClassifierPersistence = mock()
    private val llContext: LLContext = mock {
        on { ioScheduler }.thenReturn(trampoline())
        on { nowUtcMs() }.thenAnswer { _ -> timeProvider.nowUtcMs() }
    }

    private val tested = ClassificationTrigger(
        trigger = intervalObservable,
        packageClassifier = packageClassifier,
        llContext = llContext,
        classifierPersistence = classifierPersistence
    )

    @Test
    fun `registers timer with interval when started`() {
        tested.start()

        assertThat(intervalSubscriptions).isEqualTo(1)
    }

    @Test
    fun `checks time since last classification when started`() {
        tested.start()

        verify(timeProvider).nowUtcMs()
        verify(classifierPersistence).lastClassificationTimeMs
    }

    @Test
    fun `triggers classification if needed when started`() {
        val nowMs = 12321L
        givenNowMs(nowMs)
        givenLastClassificationMs(nowMs - (CLASSIFICATION_INTERVAL_MS + 1))

        tested.start()

        verify(packageClassifier).classifyWithNeuralNet()
        verify(classifierPersistence).lastClassificationTimeMs = nowMs
    }

    private fun givenNowMs(ms: Long) {
        whenever(timeProvider.nowUtcMs()).thenReturn(ms)
    }

    private fun givenLastClassificationMs(ms: Long) {
        whenever(classifierPersistence.lastClassificationTimeMs).thenReturn(ms)
    }
}