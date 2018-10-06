package com.lelloman.launcher

import android.arch.lifecycle.MutableLiveData
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.lelloman.common.utils.SingleLiveData
import com.lelloman.common.view.actionevent.ViewActionEvent
import com.lelloman.launcher.di.MockViewModelModule
import com.lelloman.launcher.testutils.TestApp
import com.lelloman.launcher.testutils.pkg
import com.lelloman.launcher.ui.AppsDrawerListItem
import com.lelloman.launcher.ui.view.MainActivity
import com.lelloman.launcher.ui.viewmodel.MainViewModel
import com.lelloman.launcher.ui.viewmodel.PackageDrawerListItem
import com.lelloman.testutils.rotateNatural
import com.lelloman.testutils.swipeBottomSheetUp
import com.lelloman.testutils.viewWithId
import com.lelloman.testutils.viewWithTextIsDisplayed
import com.lelloman.testutils.whenever
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    private val viewModelModule = MockViewModelModule()
    private lateinit var viewModel: MainViewModel

    private lateinit var drawerApps: MutableLiveData<List<AppsDrawerListItem>>
    private lateinit var classifiedApps: MutableLiveData<List<PackageDrawerListItem>>
    private lateinit var viewActionEvents: SingleLiveData<ViewActionEvent>

    @get:Rule
    val activityTestRule = ActivityTestRule(MainActivity::class.java, true, false)

    private val installedApps = Array(100) {
        PackageDrawerListItem(pkg = pkg(it))
    }.toList()

    @Before
    fun setUp() {
        rotateNatural()

        drawerApps = MutableLiveData()
        classifiedApps = MutableLiveData()
        viewActionEvents = SingleLiveData()

        viewModel = viewModelModule.mainViewModel

        whenever(viewModel.viewActionEvents).thenReturn(viewActionEvents)
        whenever(viewModel.drawerApps).thenReturn(drawerApps)
        whenever(viewModel.classifiedApps).thenReturn(classifiedApps)

        TestApp.resetPersistence()
        TestApp.dependenciesUpdate { it.viewModelModule = viewModelModule }

        activityTestRule.launchActivity(null)
    }

    @Test
    fun swipesDrawerUpAndShowsInstalledApps() {
        drawerApps.postValue(installedApps)

        viewWithId(R.id.bottom_drawer_layout).perform(swipeBottomSheetUp())

        (0..10).forEach {
            viewWithTextIsDisplayed("label $it")
        }
    }
}