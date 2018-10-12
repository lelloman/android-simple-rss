package com.lelloman.launcher.packages

import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.drawable.Drawable
import com.lelloman.common.testutils.MockResourceProvider
import com.lelloman.common.view.BroadcastReceiverWrap
import com.lelloman.launcher.classification.ClassifiedPackage
import com.lelloman.launcher.logger.LauncherLoggerFactory
import com.lelloman.launcher.persistence.db.ClassifiedIdentifierDao
import com.lelloman.launcher.persistence.db.model.ClassifiedIdentifier
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Flowable
import io.reactivex.schedulers.TestScheduler
import io.reactivex.subjects.PublishSubject
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class PackagesManagerTest {

    private val packageManager: PackageManager = mock()
    private val intentsSubject = PublishSubject.create<Intent>()
    private val classifiedIdentifierDao: ClassifiedIdentifierDao = mock()
    private val broadcastReceiverWrap: BroadcastReceiverWrap = mock {
        on { broadcasts }.thenReturn(intentsSubject)
    }
    private val queryActivityIntent: Intent = mock()
    private val ioScheduler = TestScheduler()
    private val loggerFactory: LauncherLoggerFactory = mock {
        on { getLogger(PackagesManager::class.java) }.thenReturn(mock())
    }

    private fun tested(block: PackagesManager.() -> Unit) {
        val tested = PackagesManager(
            ioScheduler = ioScheduler,
            packageManager = packageManager,
            loggerFactory = loggerFactory,
            broadcastReceiverWrap = broadcastReceiverWrap,
            queryActivityIntent = queryActivityIntent,
            launchesPackage = LAUNCHES_PACKAGE,
            mainPackage = MAIN_PACKAGE,
            classifiedPackageIdentifierDao = classifiedIdentifierDao,
            resourceProvider = MockResourceProvider()
        )
        block.invoke(tested)
    }

    @Test
    fun `registers broadcast receiver upon instantiation`() {
        givenIdentifiersDaoReturnsClassifiedIdentifiers1()
        tested {
            verify(broadcastReceiverWrap).register(
                dataScheme = "package",
                actions = arrayOf(
                    "android.intent.action.PACKAGE_ADDED",
                    "android.intent.action.PACKAGE_CHANGED",
                    "android.intent.action.PACKAGE_REMOVED",
                    "android.intent.action.PACKAGE_REPLACED"
                )
            )
        }
    }

    @Test
    fun `updates installed packages when instantiated`() {
        givenPackageManagerReturnsPackages1()
        givenIdentifiersDaoReturnsClassifiedIdentifiers1()

        tested {
            val tester = installedPackages.test()
            ioScheduler.triggerActions()

            tester.assertValueCount(1)
            tester.values()[0].let { packages ->
                assertThat(packages).hasSize(PACKAGES_1.size)
                assertThat(packages).containsAll(PACKAGES_1.toList())
            }
        }
    }

    @Test
    fun `updates classified packages when instantiated`() {
        givenPackageManagerReturnsPackages1()
        givenIdentifiersDaoReturnsClassifiedIdentifiers1()
        tested {
            val tester = classifiedPackages.test()

            ioScheduler.triggerActions()

            tester.assertValueCount(1)
            tester.values()[0].let { packages ->
                assertThat(packages).hasSize(CLASSIFIED_PACKAGES_1.size)
                assertThat(packages).containsAll(CLASSIFIED_PACKAGES_1.map { it.pkg })
            }
        }
    }

    @Test
    fun `updates installed packages when broadcast is received`() {
        givenPackageManagerReturnsPackages1()
        givenIdentifiersDaoReturnsClassifiedIdentifiers1()
        tested {
            val tester = installedPackages.test()
            ioScheduler.triggerActions()
            tester.assertValueCount(1)

            broadcastIntentPackageAdded()
            ioScheduler.triggerActions()

            tester.assertValueCount(2)
            tester.values()[1].let { packages ->
                assertThat(packages).hasSize(PACKAGES_1.size)
                assertThat(packages).containsAll(PACKAGES_1.toList())
            }
        }
    }

    @Test
    fun `updates classified packages when broadcast is received`() {
        givenPackageManagerReturnsPackages1()
        givenIdentifiersDaoReturnsClassifiedIdentifiers1()
        tested {
            val tester = classifiedPackages.test()
            ioScheduler.triggerActions()
            tester.assertValueCount(1)

            broadcastIntentPackageAdded()
            ioScheduler.triggerActions()

            tester.assertValueCount(2)
            tester.values()[1].let { packages ->
                assertThat(packages).hasSize(CLASSIFIED_PACKAGES_1.size)
                assertThat(packages).containsAll(CLASSIFIED_PACKAGES_1.map { it.pkg })
            }
        }
    }

    @Test
    fun `updates installed packages when update packages is called manually`() {
        givenPackageManagerReturnsPackages1()
        givenIdentifiersDaoReturnsClassifiedIdentifiers1()
        tested {
            val tester = installedPackages.test()
            ioScheduler.triggerActions()
            tester.assertValueCount(1)

            updateInstalledPackages()
            ioScheduler.triggerActions()

            tester.assertValueCount(2)
        }
    }

    @Test
    fun `updates classified packages when update packages is called manually`() {
        givenPackageManagerReturnsPackages1()
        givenIdentifiersDaoReturnsClassifiedIdentifiers1()
        tested {
            val tester = classifiedPackages.test()
            ioScheduler.triggerActions()
            tester.assertValueCount(1)

            updateInstalledPackages()
            ioScheduler.triggerActions()

            tester.assertValueCount(2)
        }
    }

    @Test
    fun `publish updating packages true event when starting updating packages`() {
        givenIdentifiersDaoReturnsClassifiedIdentifiers1()
        givenPackageManagerReturnsPackages1()
        tested {
            ioScheduler.triggerActions()
            val tester = updatingPackages.test()
            tester.assertValues(false)

            updateInstalledPackages()

            tester.assertValues(false, true)

            ioScheduler.triggerActions()
            tester.assertValues(false, true, false)
        }
    }

    private fun givenPackageManagerReturnsPackages1() {
        whenever(packageManager.queryIntentActivities(queryActivityIntent, 0)).thenReturn(RESOLVE_INFO_1)
    }

    private fun givenIdentifiersDaoReturnsClassifiedIdentifiers1() {
        whenever(classifiedIdentifierDao.getAll()).thenReturn(Flowable.just(CLASSIFIED_IDENTIFIERS_1))
    }

    private fun broadcastIntentPackageAdded() = intentsSubject.onNext(mockIntent("android.intent.action.PACKAGE_ADDED"))

    private fun mockIntent(action: String): Intent = mock {
        on { getAction() }.thenReturn(action)
    }

    private companion object {
        val DRAWABLE: Drawable = mock()

        fun createPackage(index: Int = 1) = Package(
            id = index.toLong(),
            label = "label $index",
            packageName = "packageName $index",
            activityName = "activityName $index",
            drawable = DRAWABLE
        )

        fun createActivityInfo(index: Int = 1): ActivityInfo {
            val activityInfo: ActivityInfo = mock()
            activityInfo.packageName = "packageName $index"
            activityInfo.name = "activityName $index"
            return activityInfo
        }


        fun createResolveInfo(index: Int = 1): ResolveInfo {
            val resolveInfo: ResolveInfo = mock()

            whenever(resolveInfo.loadLabel(any())).thenReturn("label $index")
            whenever(resolveInfo.loadIcon(any())).thenReturn(DRAWABLE)
            resolveInfo.activityInfo = createActivityInfo(index)
            return resolveInfo
        }

        private val LAUNCHES_PACKAGE = Package(
            id = -1,
            label = "mwwww",
            packageName = "meeeeow",
            activityName = "fmfmfmfmfmfmf",
            drawable = mock()
        )
        private val MAIN_PACKAGE = Package(
            id = -2,
            label = "woof",
            packageName = "badum tsss",
            activityName = "noooo",
            drawable = mock()
        )
        val RESOLVE_INFO_1 = Array(3, ::createResolveInfo).toList()
        val PACKAGES_1 = arrayOf(LAUNCHES_PACKAGE)
            .plus(Array(RESOLVE_INFO_1.size, ::createPackage).toList())
        val CLASSIFIED_IDENTIFIERS_1 = PACKAGES_1
            .mapIndexed { index, pkg ->
                ClassifiedIdentifier(
                    id = index.toLong(),
                    identifier = pkg.identifier,
                    score = index.toDouble()
                )
            }
        val CLASSIFIED_PACKAGES_1 = PACKAGES_1
            .map { pkg ->
                val score = CLASSIFIED_IDENTIFIERS_1
                    .firstOrNull { it.identifier == pkg.identifier }
                    ?.score
                    ?: 0.0
                ClassifiedPackage(
                    pkg = pkg,
                    score = score
                )
            }
    }
}
