package com.lelloman.pdfscores.persistence

import com.lelloman.pdfscores.persistence.db.AuthorsDao
import com.lelloman.pdfscores.persistence.db.PdfScoresDao
import com.lelloman.pdfscores.publicapi.PublicPdfScoresAppsFinder
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.Schedulers.trampoline
import org.junit.Test

class PdfScoresRepositoryImplTest {

    private val pdfScoresDao: PdfScoresDao = mock()
    private val authorsDao: AuthorsDao = mock()
    private val appsFinder: PublicPdfScoresAppsFinder = mock()

    private val tested by lazy {
        PdfScoresRepositoryImpl(
            pdfScoresDao = pdfScoresDao,
            authorsDao = authorsDao,
            appsFinder = appsFinder,
            newThreadScheduler = trampoline()
        )
    }

    @Test
    fun `returns empty scores list if there are no sources and an empty db`() {
        whenever(pdfScoresDao.getAll()).thenReturn(Flowable.just(emptyList()))
        whenever(appsFinder.pdfScoresApps).thenReturn(Observable.just(emptyList()))
        val tester = tested.getScores().test()

        tester.awaitCount(1)

        tester.assertValueAt(0) {
            it.isEmpty()
        }
    }

    @Test
    fun `returns empty authors list if there are no sources and an empty db`() {
        whenever(authorsDao.getAll()).thenReturn(Flowable.just(emptyList()))
        whenever(appsFinder.pdfScoresApps).thenReturn(Observable.just(emptyList()))
        val tester = tested.getAuthors().test()

        tester.awaitCount(1)

        tester.assertValueAt(0) {
            it.isEmpty()
        }
    }
}