package com.lelloman.launcher.classification

import com.lelloman.common.LLContext
import com.lelloman.common.utils.model.DayTime
import com.lelloman.common.utils.model.Time
import com.lelloman.common.utils.model.WeekTime
import com.lelloman.launcher.logger.LauncherLoggerFactory
import com.lelloman.launcher.packages.PackagesManager
import com.lelloman.launcher.persistence.PersistentClassificationInfo
import com.lelloman.launcher.persistence.db.ClassifiedIdentifierDao
import com.lelloman.launcher.persistence.db.PackageLaunchDao
import com.lelloman.launcher.persistence.db.model.PackageLaunch
import com.lelloman.nn.Network
import com.lelloman.nn.Training
import com.lelloman.nn.dataset.DataSet
import com.lelloman.nn.dataset.DataSet1D
import com.lelloman.nn.loss.Loss
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyZeroInteractions
import com.nhaarman.mockito_kotlin.whenever
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.util.*

class PackageClassifierTest {

    private val packageLaunchDao: PackageLaunchDao = mock()
    private val packagesManager: PackagesManager = mock()
    private val timeEncoder: TimeEncoder = mock {
        on { encodeDayTime(any()) }.thenReturn(doubleArrayOf(0.0))
        on { encodeWeekTime(any()) }.thenReturn(doubleArrayOf(0.0))
    }
    private val classifiedIdentifierDao: ClassifiedIdentifierDao = mock()
    private val identifierEncoder: IdentifierEncoder = mock {
        on { encode(any()) }.thenReturn(doubleArrayOf(0.0))
    }
    private val identifierEncoderProvider: IdentifierEncoderProvider = mock {
        on { provideEncoder(any()) }.thenReturn(identifierEncoder)
    }
    private val persistentClassificationInfo: PersistentClassificationInfo = mock()
    private val loggerFactory: LauncherLoggerFactory = mock { on { getLogger(PackageClassifier::class.java) }.thenReturn(mock()) }
    private val classifierPersistence: ClassifierPersistence = mock()
    private var nowMs = 0L
    private val llContext: LLContext = mock {
        on { getTime(any()) }.thenReturn(Time(WeekTime(1, 0), DayTime(0, 0)))
        on { nowUtcMs() }.thenAnswer { _ -> nowMs }
    }
    private val nnFactoryFactory: NnFactory = mock {
        on { makeClassifier(any(), any()) }.thenReturn(mock())
    }

    private val tested = PackageClassifier(
        llContext = llContext,
        packageLaunchDao = packageLaunchDao,
        packagesManager = packagesManager,
        timeEncoder = timeEncoder,
        persistentClassificationInfo = persistentClassificationInfo,
        classifiedIdentifierDao = classifiedIdentifierDao,
        identifierEncoderProvider = identifierEncoderProvider,
        loggerFactory = loggerFactory,
        classifierPersistence = classifierPersistence,
        nnFactory = nnFactoryFactory
    )

    @Test
    fun `trains classifier`() {
        givenRealNnFactory()
        val random = Random()
        val dataSize = 4
        val wholeSetSize = 1000
        val wholeSet = (0 until wholeSetSize).map { _ ->
            val inputIndex = random.nextInt(dataSize)
            val input = DoubleArray(dataSize) { index ->
                if (inputIndex == index) 1.0 else 0.0
            }
            val output = DoubleArray(dataSize) {
                var targetIndex = it + 1
                if (targetIndex >= dataSize) {
                    targetIndex = 0
                }
                input[targetIndex]
            }
            input to output
        }

        val trainingSet = DataSet1D
            .Builder(wholeSetSize)
            .add(wholeSet::get)
            .build()
        val validationSet = DataSet1D
            .Builder(wholeSetSize / 10)
            .add(wholeSet::get)
            .build()

        val network = tested.trainClassifier(
            trainingSet = trainingSet,
            validationSet = validationSet,
            identifierEncoder = mock {
                on { encodedSize }.thenReturn(dataSize)
            }
        )

        val loss = Loss.MSE.factory.invoke()
        assertThat(loss.compute(network, validationSet)).isLessThan(0.5)
    }

    @Test
    fun `has minimum training interval`() {
        givenLastTrainMs(0L)
        givenNowMs(24.hours)
        assertThat(tested.shouldRetrainClassifier()).isTrue()

        givenLastTrainMs(0L)
        givenNowMs(1.hours)
        assertThat(tested.shouldRetrainClassifier()).isFalse()

        givenLastTrainMs(0L)
        givenNowMs(12.hours)
        assertThat(tested.shouldRetrainClassifier()).isFalse()

        givenLastTrainMs(0L)
        givenNowMs(12.hours + 1)
        assertThat(tested.shouldRetrainClassifier()).isTrue()

        givenLastTrainMs(0L)
        givenNowMs((-12).hours)
        assertThat(tested.shouldRetrainClassifier()).isFalse()

        givenShouldNotTrain()
        assertThat(tested.shouldRetrainClassifier()).isFalse()

        givenShouldTrain()
        assertThat(tested.shouldRetrainClassifier()).isTrue()
    }

    @Test
    fun `returns stored classifier if should not train`() {
        val network = givenClassifierPersistenceReturnsNetwork()
        givenShouldNotTrain()

        val classifier = tested.getClassifier(listOf(mockLaunch(), mockLaunch()), identifierEncoder)

        assertThat(classifier).isEqualTo(network)
        verifyZeroInteractions(identifierEncoder)
    }

    @Test
    fun `creates and train classifier if should train`() {
        val training: Training = mock {
            on { loss }.thenReturn(mock())
        }
        whenever(nnFactoryFactory.makeTraining(any(), any(), any())).thenReturn(training)
        givenShouldTrain()

        val classifier = tested.getClassifier(LAUNCHES, identifierEncoder)

        verify(training).perform()
        verify(nnFactoryFactory).makeTraining(eq(classifier), any(), any())
    }

    private val Int.hours get() = 1000L * 60 * 60 * this

    private fun givenClassifierPersistenceReturnsNetwork() = mock<Network>().apply {
        whenever(classifierPersistence.loadClassifier()).thenReturn(this)
    }

    private fun givenShouldTrain() {
        givenLastTrainMs(0L)
        givenNowMs(Long.MAX_VALUE)
    }

    private fun givenShouldNotTrain() {
        givenLastTrainMs(0L)
        givenNowMs(12.hours)
    }

    private fun givenNowMs(ms: Long) {
        nowMs = ms
    }

    private fun givenLastTrainMs(ms: Long) {
        whenever(persistentClassificationInfo.lastTrainingTimeMs).thenReturn(ms)
    }

    private fun givenRealNnFactory() {
        whenever(nnFactoryFactory.makeClassifier(any(), any())).thenAnswer {
            NnFactory(llContext).makeClassifier(it.arguments[0] as Int, it.arguments[1] as Int)
        }

        whenever(nnFactoryFactory.makeTraining(any(), any(), any())).thenAnswer {
            NnFactory(llContext).makeTraining(
                it.arguments[0] as Network,
                it.arguments[1] as DataSet,
                it.arguments[2] as DataSet
            )
        }
    }

    private companion object {

        private fun mockLaunch(): PackageLaunch = mock {
            on { identifier() }.thenReturn("")
        }

        val LAUNCHES = listOf(mockLaunch(), mockLaunch())
    }
}