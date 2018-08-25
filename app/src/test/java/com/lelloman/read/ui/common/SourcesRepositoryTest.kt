package com.lelloman.read.ui.common

import com.lelloman.read.feed.FeedRefresher
import com.lelloman.read.persistence.db.ArticlesDao
import com.lelloman.read.persistence.db.SourcesDao
import com.lelloman.read.persistence.db.model.Article
import com.lelloman.read.persistence.db.model.Source
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyZeroInteractions
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers.trampoline
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import org.junit.Test

class SourcesRepositoryTest {

    private val sourcesDao: SourcesDao = mock()
    private val articlesDao: ArticlesDao = mock()
    private val feedRefresher: FeedRefresher = mock()

    private val tested = SourcesRepository(
        ioScheduler = trampoline(),
        sourcesDao = sourcesDao,
        feedRefresher = feedRefresher,
        articlesDao = articlesDao
    )

    @Test
    fun `loads sources`() {
        val sourcesSubject = givenAllSourcesSubject()

        val tester = tested.fetchSources().test()
        sourcesSubject.onNext(SOURCES)

        tester.assertNoErrors()
        tester.assertValue { it == SOURCES }
    }

    @Test
    fun `updates sources on new db sources event`() {
        val sourcesSubject = givenAllSourcesSubject()

        val tester = tested.fetchSources().test()
        sourcesSubject.onNext(SOURCES)
        sourcesSubject.onNext(emptyList())

        tester.assertValueCount(2)
        tester.assertValues(SOURCES, emptyList())
    }

    @Test
    fun `loads sources from db only once no matter how many subscribers`() {
        val sourcesSubject = givenAllSourcesSubject()
        val tester1 = tested.fetchSources().test()
        val tester2 = tested.fetchSources().test()
        val tester3 = tested.fetchSources().test()

        sourcesSubject.onNext(SOURCES)

        tester1.assertValues(SOURCES)
        tester2.assertValues(SOURCES)
        tester3.assertValues(SOURCES)
        verify(sourcesDao, times(1)).getAll()
    }

    @Test
    fun `inserts source and triggers feed refresh`() {
        val newId = 123456L
        whenever(sourcesDao.insert(any())).thenReturn(newId)

        val tester = tested.insertSource(mock()).test()

        tester.assertValues(newId)
        verify(feedRefresher).refresh()
    }

    @Test
    fun `deletes source and returns deleted source object`() {
        val source = SOURCES[0]
        whenever(articlesDao.getAllFromSource(source.id)).thenReturn(Flowable.just(ARTICLES))

        val tester = tested.deleteSource(source).test()

        verify(sourcesDao).delete(source.id)
        tester.assertValue { it.source == source && it.articles == ARTICLES }
        tester.assertComplete()
    }

    @Test
    fun `gets source`() {
        val source = SOURCES[0]
        whenever(sourcesDao.getSource(source.id)).thenReturn(Flowable.just(source))

        val tester = tested.getSource(source.id).test()

        tester.assertValue(source)
        tester.assertComplete()
    }

    @Test
    fun `sets source not active`() {
        val tester = tested.setSourceIsActive(1L, false).test()

        verifyZeroInteractions(feedRefresher)
        tester.assertComplete()
        verify(sourcesDao).setSourceIsActive(1L, false)
    }

    @Test
    fun `sets source active and trigger feed refresh`() {
        val tester = tested.setSourceIsActive(1L, true).test()

        verify(feedRefresher).refresh()
        tester.assertComplete()
        verify(sourcesDao).setSourceIsActive(1L, true)
    }

    private fun givenAllSourcesSubject(): Subject<List<Source>> {
        val sourcesSubject = PublishSubject.create<List<Source>>()
        whenever(sourcesDao.getAll()).thenReturn(sourcesSubject.hide().toFlowable(BackpressureStrategy.MISSING))
        return sourcesSubject
    }

    private companion object {
        val SOURCES = listOf(
            Source(
                id = 1L,
                name = "source 1",
                url = "http://www.staceppa1.com",
                lastFetched = 1L,
                isActive = true
            ),
            Source(
                id = 2L,
                name = "source 2",
                url = "http://www.staceppa2.com",
                lastFetched = 2L,
                isActive = true
            )
        )

        val ARTICLES = listOf<Article>(mock(), mock(), mock())
    }
}