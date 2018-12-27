package com.lelloman.pdfscores.persistence.assets

import com.lelloman.pdfscores.author
import com.lelloman.pdfscores.persistence.db.AuthorsDao
import com.lelloman.pdfscores.persistence.db.PdfScoresDao
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyZeroInteractions
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers.trampoline
import org.junit.Test

class AssetsCollectionInserterImplTest {

    private val assetsCollectionProvider: AssetsCollectionProvider = mock()
    private val authorsDao: AuthorsDao = mock()
    private val pdfScoresDao: PdfScoresDao = mock()

    private val tested = AssetsCollectionInserterImpl(
        assetsCollectionProvider = assetsCollectionProvider,
        authorsDao = authorsDao,
        pdfScoresDao = pdfScoresDao
    )

    @Test
    fun `does nothing if authors table is not empty`() {
        whenever(authorsDao.getAll()).thenReturn(Flowable.just(listOf(author())))
        val tester = tested.insertAssetsCollectionIntoDb().test()

        verify(authorsDao, never()).insert(any())
        verifyZeroInteractions(assetsCollectionProvider, pdfScoresDao)
        tester.assertComplete()
    }
}