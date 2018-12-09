package com.lelloman.pdfscores.persistence

import com.lelloman.pdfscores.persistence.assets.AssetsPdfScoresProvider
import com.lelloman.pdfscores.persistence.db.AuthorsDao
import com.lelloman.pdfscores.persistence.db.PdfScoresDao
import com.lelloman.pdfscores.persistence.model.Author
import com.lelloman.pdfscores.persistence.model.PdfScore
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.functions.BiFunction

class PdfScoresRepositoryImpl(
    private val pdfScoresDao: PdfScoresDao,
    private val authorsDao: AuthorsDao,
    private val assetsPdfScoresProvider: AssetsPdfScoresProvider
) : PdfScoresRepository {

    override fun getScores(): Flowable<List<PdfScore>> = Flowable
        .combineLatest(
            arrayOf(
                pdfScoresDao.getAll(),
                assetsPdfScoresProvider.pdfScores.toFlowable(BackpressureStrategy.LATEST)
            )
        ) {
            arrayOfListsOfScores ->
            val totalSum = arrayOfListsOfScores.map { (it as List<PdfScore>).size }.sum()
            ArrayList<PdfScore>(totalSum).apply {
                arrayOfListsOfScores.forEach {
                    this.addAll(it as List<PdfScore>)
                }
            }
        }

    override fun getAuthors(): Flowable<List<Author>> = Flowable
        .combineLatest(
            authorsDao.getAll(),
            assetsPdfScoresProvider.authors.toFlowable(BackpressureStrategy.LATEST),
            BiFunction<List<Author>, List<Author>, List<Author>> { authors1, authors2 ->
                ArrayList<Author>(authors1.size + authors2.size).apply {
                    addAll(authors1)
                    addAll(authors2)
                }
            }
        )
}