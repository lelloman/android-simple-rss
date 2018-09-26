package com.lelloman.deviceinfo.unit

import android.arch.lifecycle.MutableLiveData
import android.support.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.lelloman.common.utils.SingleLiveData
import com.lelloman.common.utils.model.Resolution
import com.lelloman.common.view.actionevent.ViewActionEvent
import com.lelloman.deviceinfo.infoitem.DisplayInfoItem
import com.lelloman.deviceinfo.infoitem.InfoItem
import com.lelloman.deviceinfo.testutils.InfoListScreen
import com.lelloman.deviceinfo.testutils.MockViewModelModule
import com.lelloman.deviceinfo.testutils.TestApp
import com.lelloman.deviceinfo.ui.InfoListActivity
import com.lelloman.deviceinfo.ui.InfoListViewModel
import com.lelloman.testutils.rotateNatural
import com.lelloman.testutils.whenever
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.verify

@RunWith(AndroidJUnit4::class)
class InfoListActivityTest {

    @get:Rule
    var activityTestRule = ActivityTestRule(InfoListActivity::class.java, true, false)

    private val viewModelModule = MockViewModelModule()
    private lateinit var viewModel: InfoListViewModel

    private lateinit var infoItems: MutableLiveData<List<InfoItem>>
    private lateinit var viewActionEvents: SingleLiveData<ViewActionEvent>

    private lateinit var screen: InfoListScreen

    private val screenDensityDpi = 300
    private val screenResolutionDp = Resolution(1234, 5678)
    private val screenResolutionPx = Resolution(9876, 5432)

    private val displayInfoItem: DisplayInfoItem get() = infoItemsList[0]

    private val infoItemsList = listOf(
        DisplayInfoItem(
            id = 1L,
            screenDensityDpi = screenDensityDpi,
            screenResolutionPx = screenResolutionPx,
            screenResolutionDp = screenResolutionDp
        )
    )

    @Before
    fun setup() {
        rotateNatural()

        infoItems = MutableLiveData()
        viewActionEvents = SingleLiveData()

        TestApp.dependenciesUpdate { it.viewModelModule = viewModelModule }
        viewModel = viewModelModule.infoListViewModel
        whenever(viewModel.deviceInfos).thenReturn(infoItems)
        whenever(viewModel.viewActionEvents).thenReturn(viewActionEvents)
        runOnUiThread { infoItems.postValue(infoItemsList) }

        activityTestRule.launchActivity(null)
        screen = InfoListScreen()
    }

    @Test
    fun showsDisplayInfo() {
        screen
            .showsDensityDpi(screenDensityDpi)
            .showsResolutionPx(screenResolutionPx)
            .showsResolutionDp(screenResolutionDp)
    }

    @Test
    fun handlesClickOnDisplayInfoItem() {
        screen.clickOnItem(infoItemsList.indexOf(displayInfoItem))

        verify(viewModel).onInfoItemClicked(displayInfoItem)
    }
}