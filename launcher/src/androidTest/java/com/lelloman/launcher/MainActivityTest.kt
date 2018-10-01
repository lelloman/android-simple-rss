package com.lelloman.launcher

import android.arch.lifecycle.MutableLiveData
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.lelloman.common.utils.SingleLiveData
import com.lelloman.common.view.actionevent.ViewActionEvent
import com.lelloman.launcher.di.MockViewModelModule
import com.lelloman.launcher.testutils.TestApp
import com.lelloman.launcher.ui.AppsDrawerListItem
import com.lelloman.launcher.ui.view.MainActivity
import com.lelloman.launcher.ui.viewmodel.MainViewModel
import com.lelloman.testutils.rotateNatural
import com.lelloman.testutils.whenever
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

    }
}