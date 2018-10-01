package com.lelloman.launcher

import android.arch.lifecycle.MutableLiveData
import android.support.test.espresso.UiController
import android.support.test.espresso.ViewAction
import android.support.test.espresso.action.MotionEvents
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.view.View
import com.lelloman.common.utils.SingleLiveData
import com.lelloman.common.view.actionevent.ViewActionEvent
import com.lelloman.launcher.di.MockViewModelModule
import com.lelloman.launcher.testutils.TestApp
import com.lelloman.launcher.ui.AppsDrawerListItem
import com.lelloman.launcher.ui.view.MainActivity
import com.lelloman.launcher.ui.viewmodel.MainViewModel
import com.lelloman.testutils.rotateNatural
import com.lelloman.testutils.viewWithId
import com.lelloman.testutils.wait
import com.lelloman.testutils.whenever
import org.hamcrest.Matcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    private val viewModelModule = MockViewModelModule()
    private lateinit var viewModel: MainViewModel

    private lateinit var packages: MutableLiveData<List<AppsDrawerListItem>>
    private lateinit var viewActionEvents: SingleLiveData<ViewActionEvent>

    @get:Rule
    val activityTestRule = ActivityTestRule(MainActivity::class.java, true, false)

    @Before
    fun setUp() {
        rotateNatural()

        packages = MutableLiveData()
        viewActionEvents = SingleLiveData()

        viewModel = viewModelModule.mainViewModel

        whenever(viewModel.viewActionEvents).thenReturn(viewActionEvents)
        whenever(viewModel.packages).thenReturn(packages)

        TestApp.resetPersistence()
        TestApp.dependenciesUpdate { it.viewModelModule = viewModelModule }

        activityTestRule.launchActivity(null)
    }

    @Test
    fun asd(){
        viewWithId(R.id.header).perform(object : ViewAction {
            override fun getDescription(): String {
                return ""
            }

            override fun getConstraints(): Matcher<View> {
                return isDisplayed()
            }

            override fun perform(uiController: UiController, view: View) {
                val location = floatArrayOf(200f, 1600f)
                val coordinates = floatArrayOf(location[0] + 10f, location[1] + 10f)
                val coordinatesUp = floatArrayOf(location[0] + 10f, location[1] - 600f)
                val precision = floatArrayOf(1f, 1f)
                val down = MotionEvents.sendDown(uiController, coordinates, precision).down
                uiController.loopMainThreadForAtLeast(200)
                MotionEvents.sendMovement(uiController, down, coordinatesUp)
                wait(0.02)
                MotionEvents.sendUp(uiController, down, coordinatesUp)
            }

        })

        wait(10.0)
    }
}