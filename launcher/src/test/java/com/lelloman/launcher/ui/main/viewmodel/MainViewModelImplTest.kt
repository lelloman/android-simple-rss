package com.lelloman.launcher.ui.main.viewmodel

import android.arch.lifecycle.Observer
import com.lelloman.common.utils.TimeProvider
import com.lelloman.common.viewmodel.BaseViewModel
import com.lelloman.launcher.classification.ClassifiedPackage
import com.lelloman.launcher.logger.LauncherLoggerFactory
import com.lelloman.launcher.packages.Package
import com.lelloman.launcher.packages.PackagesManager
import com.lelloman.launcher.persistence.db.PackageLaunchDao
import com.lelloman.launcher.testutils.AndroidArchTest
import com.lelloman.launcher.ui.main.AppsDrawerListItem
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.argThat
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers.trampoline
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class MainViewModelImplTest : AndroidArchTest() {

    private val packagesManager: PackagesManager = mock()
    private val dependencies: BaseViewModel.Dependencies = mock()
    private val packageLaunchDao: PackageLaunchDao = mock()
    private val timeProvider: TimeProvider = mock()
    private val loggerFactory: LauncherLoggerFactory = mock {
        on { getLogger(any()) }.thenReturn(mock())
    }

    private lateinit var tested: MainViewModelImpl

    override fun setUp() {
        tested = MainViewModelImpl(
            ioScheduler = trampoline(),
            packagesManager = packagesManager,
            dependencies = dependencies,
            packageLaunchDao = packageLaunchDao,
            timeProvider = timeProvider,
            launcherLoggerFactoryFactory = loggerFactory
        )
    }

    @Test
    fun `receives drawer apps`() {
        val observer: Observer<List<AppsDrawerListItem>> = mock()
        givenPackagesManagerReturnsClassifiedPackages()

        tested.drawerApps.observeForever(observer)

        verify(observer).onChanged(argThat {
            assertThat(this).hasSize(CLASSIFIED_PACKAGES.size + 1)
            assertThat(this.filter { it is SearchDrawerListItem }).hasSize(1)
            true
        })
    }

    private fun givenPackagesManagerReturnsClassifiedPackages() {
        whenever(packagesManager.classifiedPackages).thenReturn(Observable.just(CLASSIFIED_PACKAGES))
    }

    private companion object {

        private fun pkg(index: Int = 1) = Package(
            id = index.toLong(),
            label = "$index",
            packageName = "$index",
            activityName = "$index",
            drawable = mock()
        )

        private fun classifiedPkg(index: Int = 1) = ClassifiedPackage(
            pkg = pkg(index),
            score = index.toDouble()
        )

        val CLASSIFIED_PACKAGES = Array(11, ::pkg).toList()

    }
}