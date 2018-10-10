package com.lelloman.launcher.classification

import com.lelloman.common.utils.TimeProvider
import com.lelloman.launcher.packages.PackagesManager
import com.lelloman.launcher.persistence.PackageLaunchDao
import com.nhaarman.mockito_kotlin.mock
import org.junit.Test

class PackageClassifierImplTest {

    private val packageLaunchDao: PackageLaunchDao = mock()
    private val packagesManager: PackagesManager = mock()
    private val timeProvider: TimeProvider = mock()
    private val timeEncoder: TimeEncoder = mock()
    private val launchesEncoder: IdentifierEncoder = mock()
//
//    private val tested = PackageClassifier(
//        packageLaunchDao = packageLaunchDao,
//        timeProvider = timeProvider,
//        timeEncoder = timeEncoder,
//        loggerFactory = MockLoggerFactory(),
//        launchesEncoderProvider = { launchesEncoder },
//        packagesManager = packagesManager
//    )

    @Test
    fun `classifies with neural net`() {
        throw PleaseWriteThisUnitTestException()
    }

    class PleaseWriteThisUnitTestException : Exception()
}