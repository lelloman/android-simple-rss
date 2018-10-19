package com.lelloman.launcher

import android.arch.lifecycle.Observer
import android.arch.persistence.room.Room
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.drawable.Drawable
import android.support.test.InstrumentationRegistry
import android.support.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import android.support.test.runner.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.lelloman.common.di.BaseApplicationModule
import com.lelloman.common.utils.TimeProvider
import com.lelloman.common.view.ResourceProvider
import com.lelloman.common.viewmodel.BaseViewModel
import com.lelloman.launcher.classification.ClassifiedPackage
import com.lelloman.launcher.di.ViewModelModule
import com.lelloman.launcher.logger.LauncherLoggerFactory
import com.lelloman.launcher.packages.Package
import com.lelloman.launcher.packages.PackagesManager
import com.lelloman.launcher.packages.PackagesModule
import com.lelloman.launcher.persistence.PersistenceModule
import com.lelloman.launcher.persistence.db.AppDatabase
import com.lelloman.launcher.persistence.db.PackageLaunchDao
import com.lelloman.launcher.persistence.db.model.ClassifiedIdentifier
import com.lelloman.launcher.testutils.MockPackageManager
import com.lelloman.launcher.testutils.TestApp
import com.lelloman.launcher.ui.main.AppsDrawerListItem
import com.lelloman.launcher.ui.main.viewmodel.MainViewModel
import com.lelloman.launcher.ui.main.viewmodel.PackageDrawerListItem
import io.reactivex.Scheduler
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import java.lang.System.currentTimeMillis
import java.lang.Thread.sleep


@RunWith(AndroidJUnit4::class)
class MainViewIntegrationModelTest {

    private lateinit var tested: MainViewModel

    private val mockPackageManager = MockPackageManager()
    private lateinit var db: AppDatabase

    private val baseApplicationModule by lazy {
        object : BaseApplicationModule(TestApp.instance) {
            override fun providePackageManager(context: Context): PackageManager = mockPackageManager
        }
    }

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
            .apply { db = this }
    }

    private val packagesModule = object : PackagesModule() {
        override fun provideLaunchesActivityPackage(context: Context, resourceProvider: ResourceProvider) =
            LAUNCHES_ACTIVITY_PACKAGE
    }

    private lateinit var viewModel: MainViewModel

    @Before
    fun setUp() {
        mockPackageManager.queryIntentActivitiesResult = INSTALLED_PACKAGES.toMutableList()
        TestApp.dependenciesUpdate {
            it.viewModelModule = viewModelModule
            it.persistenceModule = persistenceModule
            it.baseApplicationModule = baseApplicationModule
            it.packagesModule = packagesModule
        }
        viewModel = TestApp.instance.viewModelFactory.create(MainViewModel::class.java)
    }

    @Test
    fun showsAppsDrawer() {
        val tester = AppsDrawerObserver()
        viewModel.drawerApps.observeForever(tester)
        waitUntil { tester.hasValues() }

        // +2 for search list item and LLLaunches
        val expectedSize = INSTALLED_PACKAGES.size + 2
        runOnUiThread { assertThat(tester.items).hasSize(expectedSize) }
    }

    @Test
    fun showsAppsDrawerWithClassifiedOrdering() {
        givenClassifiedIdentifiersInDb()
        val tester = AppsDrawerObserver()
        viewModel.drawerApps.observeForever(tester)
        waitUntil { tester.hasValues() }

        // +2 for search list item and LLLaunches
        val expectedSize = INSTALLED_PACKAGES.size + 2
        val expectedItems = INSTALLED_PACKAGES
            .reversed()
            .asSequence()
            .map { PackageDrawerListItem(it.toPackage()) }
            .plus(PackageDrawerListItem(LAUNCHES_ACTIVITY_PACKAGE))
            .toList()
        runOnUiThread {
            assertThat(tester.items).hasSize(expectedSize)
            val actualItems = tester
                .items!!
                .filter { it is PackageDrawerListItem }
                .map { it as PackageDrawerListItem }
            expectedItems.forEachIndexed { index, item ->
                assertThat(actualItems[index].pkg.identifier())
                    .isEqualTo(item.pkg.identifier())
            }
        }
    }

    @Test
    fun updatesAppsDrawerWithClassifiedOrdering() {
        val tester = AppsDrawerObserver()
        viewModel.drawerApps.observeForever(tester)
        waitUntil { tester.hasValues() }

        // +2 for search list item and LLLaunches
        val expectedSize = INSTALLED_PACKAGES.size + 2
        val expectedStraightItems = INSTALLED_PACKAGES
            .asSequence()
            .map { PackageDrawerListItem(it.toPackage()) }
            .plus(PackageDrawerListItem(LAUNCHES_ACTIVITY_PACKAGE))
            .toList()
        runOnUiThread {
            assertThat(tester.items).hasSize(expectedSize)
            val actualItems = tester
                .items!!
                .filter { it is PackageDrawerListItem }
                .map { it as PackageDrawerListItem }
            expectedStraightItems.forEachIndexed { index, item ->
                assertThat(actualItems[index].pkg.identifier())
                    .isEqualTo(item.pkg.identifier())
            }
        }
        tester.reset()
        givenClassifiedIdentifiersInDb()
        waitUntil { tester.hasValues() }

        val expectedReversedItems = INSTALLED_PACKAGES
            .reversed()
            .asSequence()
            .map { PackageDrawerListItem(it.toPackage()) }
            .plus(PackageDrawerListItem(LAUNCHES_ACTIVITY_PACKAGE))
            .toList()
        runOnUiThread {
            assertThat(tester.items).hasSize(expectedSize)
            val actualItems = tester
                .items!!
                .filter { it is PackageDrawerListItem }
                .map { it as PackageDrawerListItem }
            expectedReversedItems.forEachIndexed { index, item ->
                assertThat(actualItems[index].pkg.identifier())
                    .isEqualTo(item.pkg.identifier())
            }
        }
    }

    private fun givenClassifiedIdentifiersInDb() {
        db.classifiedIdentifierDao().insert(CLASSIFIED_IDENTIFIERS)
    }

    class AppsDrawerObserver : Observer<List<AppsDrawerListItem>> {
        var items: MutableList<AppsDrawerListItem>? = null

        override fun onChanged(t: List<AppsDrawerListItem>?) {
            items = t?.toMutableList()
        }

        fun hasValues() = items?.isNotEmpty() == true

        fun reset() = items?.clear()

    }

    private companion object {

        const val WAIT_UNTIL_TIMEOUT_MS = 1000L
        val LAUNCHES_ACTIVITY_PACKAGE = Package(
            id = -1,
            label = "lau lau launch",
            packageName = "com.fritta.staceppa",
            activityName = "Gino",
            drawable = mock(Drawable::class.java)
        )

        private fun waitUntil(predicate: () -> Boolean) {
            val startTime = System.currentTimeMillis()
            while (!predicate.invoke() && currentTimeMillis() - startTime < WAIT_UNTIL_TIMEOUT_MS) {
                sleep(10)
            }
        }

        fun resolveInfo(index: Int = 1) = object : ResolveInfo() {
            override fun loadLabel(pm: PackageManager?): CharSequence = "label $index"
            override fun loadIcon(pm: PackageManager?): Drawable {
                return mock(Drawable::class.java)
            }
        }.apply {
            activityInfo = ActivityInfo().apply {
                packageName = "asd.asd.$index"
                name = "asd.asd.$index.Asd"
            }
        }

        val INSTALLED_PACKAGES = Array(10, ::resolveInfo)

        val CLASSIFIED_IDENTIFIERS = INSTALLED_PACKAGES
            .mapIndexed { index, resolveInfo ->
                ClassifiedPackage(
                    pkg = resolveInfo.toPackage(),
                    score = index.toDouble()
                )
            }
            .map {
                ClassifiedIdentifier(
                    id = it.pkg.id,
                    identifier = it.pkg.identifier(),
                    score = it.score
                )
            }
            .toList()

        private fun ResolveInfo.toPackage() = Package(
            id = hashCode().toLong(),
            label = activityInfo.labelRes.toString(),
            packageName = activityInfo.packageName,
            activityName = activityInfo.name,
            drawable = mock(Drawable::class.java)
        )
    }
}