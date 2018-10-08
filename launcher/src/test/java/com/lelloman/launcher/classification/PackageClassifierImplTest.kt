package com.lelloman.launcher.classification

import com.lelloman.common.testutils.MockLoggerFactory
import com.lelloman.common.utils.TimeProvider
import com.lelloman.launcher.persistence.PackageLaunchDao
import com.nhaarman.mockito_kotlin.mock
import org.junit.Test

class PackageClassifierImplTest {

    private val packageLaunchDao: PackageLaunchDao = mock()
    private val neuralNetFactory: NeuralNetFactory = mock()
    private val timeProvider: TimeProvider = mock()
    private val timeEncoder: TimeEncoder = mock()

    private val tested = PackageClassifierImpl(
        packageLaunchDao = packageLaunchDao,
        neuralNetFactory = neuralNetFactory,
        timeProvider = timeProvider,
        timeEncoder = timeEncoder,
        loggerFactory = MockLoggerFactory()
    )

    @Test
    fun `classifies with neural net`() {
        TODO("implement me")
    }
}