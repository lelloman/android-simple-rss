package com.lelloman.pdfscores.persistence

import com.lelloman.pdfscores.persistence.db.AuthorsDao
import com.lelloman.pdfscores.persistence.db.PdfScoresDao
import com.lelloman.pdfscores.persistence.model.Author
import com.lelloman.pdfscores.persistence.model.PdfScore
import com.lelloman.pdfscores.publicapi.PublicPdfScoresAppsFinder
import io.reactivex.Flowable
import io.reactivex.Scheduler

class PdfScoresRepositoryImpl(
    private val pdfScoresDao: PdfScoresDao,
    private val authorsDao: AuthorsDao,
    private val appsFinder: PublicPdfScoresAppsFinder,
    private val newThreadScheduler: Scheduler
) : PdfScoresRepository {

    override fun getScores(): Flowable<List<PdfScore>> = pdfScoresDao
        .getAll()
        .map { it as List<PdfScore> }

    override fun getAuthors(): Flowable<List<Author>> = authorsDao.getAll()
}