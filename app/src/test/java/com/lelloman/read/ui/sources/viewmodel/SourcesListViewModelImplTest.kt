package com.lelloman.read.ui.sources.viewmodel

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import com.google.common.truth.Truth.assertThat
import com.lelloman.read.core.navigation.NavigationScreen
import com.lelloman.read.core.navigation.ScreenNavigationEvent
import com.lelloman.read.core.view.ViewActionEvent
import com.lelloman.read.persistence.db.model.Source
import com.lelloman.read.persistence.settings.clear
import com.lelloman.read.testutils.MockResourceProvider
import com.lelloman.read.ui.articles.repository.ArticlesRepository
import com.lelloman.read.ui.sources.repository.SourcesRepository
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.argWhere
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers.trampoline
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class SourcesListViewModelImplTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private val sourcesRepository: SourcesRepository = mock()
    private val articlesRepository: ArticlesRepository = mock()
    private val resourceProvider = MockResourceProvider()

    private val tested = SourcesListViewModelImpl(
        ioScheduler = trampoline(),
        uiScheduler = trampoline(),
        sourcesRepository = sourcesRepository,
        articlesRepository = articlesRepository,
        resourceProvider = resourceProvider
    )

    @Test
    fun `fetches sources`() {
        whenever(sourcesRepository.fetchSources()).thenReturn(Observable.just(SOURCES))

        val sources = tested.sources.value

        assertThat(sources).isEqualTo(SOURCES)
    }

    @Test
    fun `un-subscribes on cleared`() {
        val sourcesSubject = givenSourcesSubject()
        val sources = tested.sources.value // create susbcription
        assertThat(sourcesSubject.hasObservers()).isTrue()

        tested.clear()

        assertThat(sourcesSubject.hasObservers()).isFalse()
    }

    @Test
    fun `navigates to add source when click on fab`() {
        val viewActionObserver: Observer<ViewActionEvent> = mock()
        tested.viewActionEvents.observeForever(viewActionObserver)

        tested.onFabClicked(mock())

        verify(viewActionObserver).onChanged(argWhere {
            it is ScreenNavigationEvent && it.targetClass == NavigationScreen.ADD_SOURCE
        })
    }

    @Test
    fun `disable source when click and is active`() {
        whenever(sourcesRepository.getSource(ACTIVE_SOURCE.id)).thenReturn(Flowable.just(ACTIVE_SOURCE))
        whenever(sourcesRepository.setSourceIsActive(any(), any())).thenReturn(Completable.complete())

        tested.onSourceClicked(ACTIVE_SOURCE.id)

        verify(sourcesRepository).setSourceIsActive(ACTIVE_SOURCE.id, false)
    }

    @Test
    fun `enable source when click and is not active`() {
        whenever(sourcesRepository.getSource(INACTIVE_SOURCE.id)).thenReturn(Flowable.just(INACTIVE_SOURCE))
        whenever(sourcesRepository.setSourceIsActive(any(), any())).thenReturn(Completable.complete())

        tested.onSourceClicked(INACTIVE_SOURCE.id)

        verify(sourcesRepository).setSourceIsActive(INACTIVE_SOURCE.id, true)
    }

    private fun givenSourcesSubject(): Subject<List<Source>> {
        val subject = PublishSubject.create<List<Source>>()
        whenever(sourcesRepository.fetchSources()).thenReturn(subject)
        return subject
    }

    private companion object {

        val ACTIVE_SOURCE = Source(
            id = 1L,
            name = "Active source",
            url = "url",
            lastFetched = 0L,
            isActive = true
        )

        val INACTIVE_SOURCE = Source(
            id = 1L,
            name = "Inactive source",
            url = "url",
            lastFetched = 0L,
            isActive = false
        )

        val SOURCES = listOf(
            Source(
                id = 1L,
                name = "Source 1",
                url = "url 1",
                lastFetched = 1L,
                isActive = true
            ),
            Source(
                id = 2L,
                name = "Source 2",
                url = "url 2",
                lastFetched = 2L,
                isActive = true
            )
        )
    }
}