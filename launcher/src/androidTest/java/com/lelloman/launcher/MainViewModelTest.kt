package com.lelloman.launcher

import android.arch.persistence.room.Room
import android.content.Context
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.lelloman.common.utils.TimeProvider
import com.lelloman.common.viewmodel.BaseViewModel
import com.lelloman.launcher.di.ViewModelModule
import com.lelloman.launcher.logger.LauncherLoggerFactory
import com.lelloman.launcher.packages.PackagesManager
import com.lelloman.launcher.persistence.PersistenceModule
import com.lelloman.launcher.persistence.db.AppDatabase
import com.lelloman.launcher.persistence.db.PackageLaunchDao
import com.lelloman.launcher.testutils.TestApp
import com.lelloman.launcher.ui.main.viewmodel.MainViewModel
import io.reactivex.Scheduler
import org.junit.Before
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainViewModelTest {

    private lateinit var tested: MainViewModel

    private val viewModelModule = object : ViewModelModule() {
        override fun provideMainViewModel(ioScheduler: Scheduler, packagesManager: PackagesManager, dependencies: BaseViewModel.Dependencies, packageLaunchDao: PackageLaunchDao, timeProvider: TimeProvider, launcherLoggerFactory: LauncherLoggerFactory): MainViewModel {
            tested = super.provideMainViewModel(ioScheduler, packagesManager, dependencies, packageLaunchDao, timeProvider, launcherLoggerFactory)
            return tested
        }
    }

    private val persistenceModule = object : PersistenceModule() {
        override fun provideDatabase(context: Context) = Room
            .inMemoryDatabaseBuilder(InstrumentationRegistry.getTargetContext(), AppDatabase::class.java)
            .build()
    }

    @Before
    fun setUp() {
        TestApp.dependenciesUpdate {
            it.viewModelModule = viewModelModule
            it.persistenceModule = persistenceModule
        }
    }
}