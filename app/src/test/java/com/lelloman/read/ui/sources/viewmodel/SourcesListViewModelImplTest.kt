package com.lelloman.read.ui.sources.viewmodel

import android.arch.lifecycle.Observer
import com.google.common.truth.Truth.assertThat
import com.lelloman.read.R
import com.lelloman.read.core.ActionTokenProvider
import com.lelloman.read.core.navigation.NavigationScreen
import com.lelloman.read.core.navigation.ScreenNavigationEvent
import com.lelloman.read.core.view.actionevent.SnackEvent
import com.lelloman.read.core.view.actionevent.ToastEvent
import com.lelloman.read.core.view.actionevent.ViewActionEvent
import com.lelloman.read.persistence.db.model.Article
import com.lelloman.read.persistence.db.model.Source
import com.lelloman.read.persistence.settings.clear
import com.lelloman.read.testutils.AndroidArchTest
import com.lelloman.read.testutils.MockResourceProvider
import com.lelloman.read.testutils.test
import com.lelloman.read.ui.common.repository.ArticlesRepository
import com.lelloman.read.ui.common.repository.DeletedSource
import com.lelloman.read.ui.common.repository.SourcesRepository
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.argWhere
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyZeroInteractions
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers.trampoline
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import org.junit.Test

class SourcesListViewModelImplTest : AndroidArchTest() {

    private val sourcesRepository: SourcesRepository = mock()
    private val articlesRepository: ArticlesRepository = mock()
    private val resourceProvider = MockResourceProvider()
    private val actionTokenProvider: ActionTokenProvider = mock()

    private lateinit var tested: SourcesListViewModelImpl

    override fun setUp() {
        tested = SourcesListViewModelImpl(
            ioScheduler = trampoline(),
            uiScheduler = trampoline(),
            sourcesRepository = sourcesRepository,
            articlesRepository = articlesRepository,
            resourceProvider = resourceProvider,
            actionTokenProvider = actionTokenProvider
        )
    }

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
            it is ScreenNavigationEvent && it.screen == NavigationScreen.ADD_SOURCE
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

    @Test
    fun `deletes source and shows snack from repository on source swiped`() {
        val source = SOURCES[0]
        val viewActions = tested.viewActionEvents.test()
        whenever(sourcesRepository.deleteSource(any())).thenReturn(Single.just(mock()))
        val token = "poppopopopopopop"
        givenNextActionToken(token)

        tested.onSourceSwiped(source)

        verify(sourcesRepository).deleteSource(source)
        viewActions.assertValues(SnackEvent(
            message = "${R.string.source_deleted}:${source.name}",
            actionLabel = "${R.string.undo}",
            actionToken = token
        ))
    }

    @Test
    fun `shows toast if source deletion fails`() {
        whenever(sourcesRepository.deleteSource(any())).thenReturn(Single.error(Exception()))
        val viewActions = tested.viewActionEvents.test()

        tested.onSourceSwiped(mock())

        viewActions.assertValues(ToastEvent(message = "${R.string.something_went_wrong}"))
    }

    @Test
    fun `re-insert source and articles on token action`() {
        val recreatedSourceId = 2394870623
        val deletedSource = DeletedSource(
            source = SOURCES[0],
            articles = listOf(
                Article(
                    id = 1L,
                    title = "article 1",
                    subtitle = "subtitle 1",
                    content = "content 1",
                    link = "link 1",
                    imageUrl = "img url 1",
                    time = 1L,
                    sourceId = 0
                ),
                Article(
                    id = 2L,
                    title = "article 2",
                    subtitle = "subtitle 2",
                    content = "content 2",
                    link = "link 2",
                    imageUrl = "img url 2",
                    time = 2L,
                    sourceId = 0
                )
            )
        )
        givenCanDeleteSource(deletedSource)
        val actionToken = "peppereppeppe"
        givenNextActionToken(actionToken)
        givenCanInsertSource(recreatedSourceId)
        tested.onSourceSwiped(deletedSource.source)

        tested.onTokenAction(actionToken)

        verify(sourcesRepository).insertSource(deletedSource.source)
        verify(articlesRepository).insertArticles(
            deletedSource.articles.map {
                it.copy(sourceId = recreatedSourceId)
            }
        )
    }

    @Test
    fun `does nothing on non existing token action`() {
        tested.onTokenAction("badum tssss")

        verifyZeroInteractions(sourcesRepository, articlesRepository)
    }

    @Test
    fun `shows toast if insert source fails`() {
        val viewActions = tested.viewActionEvents.test()
        val token = "asdomar"
        val source = SOURCES[0]
        givenCanDeleteSource(DeletedSource(source = source, articles = emptyList()))
        givenNextActionToken(token)
        givenCanInsertArticles()
        whenever(sourcesRepository.insertSource(any())).thenReturn(Single.error(Exception()))
        givenCanInsertArticles()
        tested.onSourceSwiped(source)
        viewActions.resetValues()

        tested.onTokenAction(token)

        viewActions.assertValues(
            ToastEvent("${R.string.something_went_wrong}")
        )
    }

    @Test
    fun `shows toast if insert articles failes`() {
        val viewActions = tested.viewActionEvents.test()
        val token = "asdomar"
        val source = SOURCES[0]
        givenCanDeleteSource(DeletedSource(source = source, articles = emptyList()))
        givenNextActionToken(token)
        givenCanInsertSource(123)
        whenever(articlesRepository.insertArticles(any())).thenReturn(Single.error(Exception()))
        tested.onSourceSwiped(source)
        viewActions.resetValues()

        tested.onTokenAction(token)

        viewActions.assertValues(
            ToastEvent("${R.string.something_went_wrong}")
        )
    }

    private fun givenCanInsertArticles() {
        whenever(articlesRepository.insertArticles(any())).thenAnswer {
            val nArticles = (it.arguments[0] as List<Article>).size
            Single.just(Array(nArticles) { it.toLong() })
        }
    }

    private fun givenCanDeleteSource(deletedSource: DeletedSource = mock()) {
        whenever(sourcesRepository.deleteSource(any())).thenReturn(Single.just(deletedSource))
    }

    private fun givenCanInsertSource(newId: Long) {
        whenever(sourcesRepository.insertSource(any())).thenReturn(Single.just(newId))
    }

    private fun givenNextActionToken(actionToken: String) {
        whenever(actionTokenProvider.makeActionToken()).thenReturn(actionToken)
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