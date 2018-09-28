package com.lelloman.deviceinfo

import android.support.test.InstrumentationRegistry.getInstrumentation
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.lelloman.common.utils.model.Resolution
import com.lelloman.deviceinfo.device.DeviceImpl
import com.lelloman.deviceinfo.testutils.TestApp
import com.lelloman.deviceinfo.ui.view.InfoListActivity
import com.lelloman.testutils.rotateLeft
import com.lelloman.testutils.rotateNatural
import io.reactivex.observers.TestObserver
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * To be run on Nexus5X emulator
 */
@RunWith(AndroidJUnit4::class)
class DeviceImplTest {

    @get:Rule
    val activityTestRule = ActivityTestRule(InfoListActivity::class.java, true, true)

    private val tested = DeviceImpl(
        context = getInstrumentation().targetContext,
        configurationChanges = TestApp.configurationChanges
    )

    private lateinit var configurationChangesTester: TestObserver<Any>

    @Before
    fun setUp() {
        configurationChangesTester = TestApp.configurationChanges.test()
    }

    @After
    fun tearDown() {
        rotateNatural()
    }

    @Test
    fun returnsDisplayData() {
        rotateNatural()
        val resolutionTester = tested.screenResolutionPx.test()
        val dpiTester = tested.screenDensityDpi.test()
        val sizeDpTester = tested.screenResolutionDp.test()

        resolutionTester.assertValues(Resolution(1080, 1920))
        dpiTester.assertValues(420)
        sizeDpTester.assertValues(Resolution(411, 731))

        rotateLeft()
        waitForConfigurationChange()

        resolutionTester.assertValueAt(1, Resolution(1920, 1080))
        dpiTester.assertValueAt(1, 420)
        sizeDpTester.assertValueAt(1, Resolution(731, 411))
    }

    private fun waitForConfigurationChange() = configurationChangesTester.awaitCount(1, {}, 2000)
}