package com.lelloman.simplerss.unit

import android.arch.lifecycle.MutableLiveData
import android.support.test.rule.ActivityTestRule
import com.lelloman.common.view.AppTheme
import com.lelloman.common.view.actionevent.ViewActionEvent
import com.lelloman.instrumentedtestutils.rotateNatural
import com.lelloman.instrumentedtestutils.whenever
import com.lelloman.simplerss.di.MockViewModelModule
import com.lelloman.simplerss.testutils.TestApp
import com.lelloman.simplerss.testutils.screen.SourcesListScreen
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SourcesListActivityTest {

    @get:Rule
    val rule = ActivityTestRule<com.lelloman.simplerss.ui.sources.view.SourcesListActivity>(com.lelloman.simplerss.ui.sources.view.SourcesListActivity::class.java, true, false)

    private val viewModelModule = MockViewModelModule()
    private lateinit var viewModel: com.lelloman.simplerss.ui.sources.viewmodel.SourcesListViewModel

    private lateinit var sources: MutableLiveData<List<com.lelloman.simplerss.persistence.db.model.Source>>
    private lateinit var viewActionEvents: Subject<ViewActionEvent>
    private lateinit var emptyViewVisible: MutableLiveData<Boolean>

    @Before
    fun setUp() {
        rotateNatural()

        sources = MutableLiveData()
        emptyViewVisible = MutableLiveData()
        viewActionEvents = PublishSubject.create()

        viewModel = viewModelModule.sourcesListViewModel
        whenever(viewModel.themeChangedEvents).thenReturn(PublishSubject.create<AppTheme>().hide())
        whenever(viewModel.sources).thenReturn(sources)
        whenever(viewModel.viewActionEvents).thenReturn(viewActionEvents.hide())
        whenever(viewModel.emptyViewVisible).thenReturn(emptyViewVisible)

        TestApp.resetPersistence()
        TestApp.dependenciesUpdate { it.viewModelModule = viewModelModule }
    }

    @Test
    fun showsEmptyUi() {
        sources.postValue(emptyList())
        emptyViewVisible.postValue(true)
        launchActivity()

        SourcesListScreen()
            .showsFab()
            .showsSources(0)
            .showsEmptyListMessage()
    }

    @Test
    fun showsListOfSources() {
        val sourcesFromDb = Array(10, ::source).toList()

        sources.postValue(sourcesFromDb)
        emptyViewVisible.postValue(false)
        launchActivity()

        SourcesListScreen()
            .doesntShowEmptyListMessage()
            .showsSources(sourcesFromDb.size)

    }

    private fun launchActivity() = rule.launchActivity(null)

    private fun source(index: Int) = com.lelloman.simplerss.persistence.db.model.Source(
        id = index.toLong(),
        name = "source $index",
        url = "www.$index.com",
        lastFetched = index.toLong(),
        isActive = true,
        favicon = null
    )
}