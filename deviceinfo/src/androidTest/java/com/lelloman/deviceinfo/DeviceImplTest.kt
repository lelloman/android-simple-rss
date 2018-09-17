package com.lelloman.deviceinfo

import android.support.test.InstrumentationRegistry.getInstrumentation
import android.support.test.runner.AndroidJUnit4
import com.lelloman.common.utils.model.Resolution
import com.lelloman.deviceinfo.device.DeviceImpl
import org.junit.Test
import org.junit.runner.RunWith

/**
 * To be run on Nexus5X emulator
 */
@RunWith(AndroidJUnit4::class)
class DeviceImplTest {

    private val tested = DeviceImpl(getInstrumentation().context)

    @Test
    fun returnsDisplayData() {
        val resolutionTester = tested.screenResolutionPx.test()
        val dpiTester = tested.screenDensityDpi.test()
        val sizeDpTester = tested.screenResolutionDp.test()

        resolutionTester.assertValues(Resolution(1080, 1920))
        dpiTester.assertValues(420)
        sizeDpTester.assertValues(Resolution(411, 731))
    }
}