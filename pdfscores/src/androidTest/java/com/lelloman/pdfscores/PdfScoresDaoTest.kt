package com.lelloman.pdfscores

import android.arch.persistence.room.Room
import android.database.sqlite.SQLiteConstraintException
import android.support.test.InstrumentationRegistry.getTargetContext
import com.lelloman.pdfscores.persistence.db.AppDatabase
import com.lelloman.pdfscores.persistence.db.AuthorsDao
import com.lelloman.pdfscores.persistence.db.PdfScoresDao
import com.lelloman.pdfscores.persistence.model.Author
import com.lelloman.pdfscores.persistence.model.PdfScore
import org.assertj.core.api.Assertions.assertThatThrownBy
import com.lelloman.pdfscores.testutils.pdfScore
import com.lelloman.pdfscores.testutils.author
import org.junit.Before
import org.junit.Test

class PdfScoresDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var tested: PdfScoresDao
    private lateinit var authorsDao: AuthorsDao

    private var testerCount = 0

    @Before
    fun setUp() {
        val app = getTargetContext().applicationContext as PdfScoresApplication
        db = Room.inMemoryDatabaseBuilder(app, AppDatabase::class.java).build()
        tested = db.pdfScoresDao()
        authorsDao = db.authorsDao()
    }

    @Test
    fun returnsEmptyListWhenCreated() {
        val tester = tested.getAll().test()

        tester.awaitCount(++testerCount)

        tester.assertValueAt(testerCount - 1) { it.isEmpty() }
    }

    @Test
    fun throwsExceptionWhenAddingPdfScoreWithInvalidAuthorId() {
        val tester = tested.getAll().test()
        tester.awaitCount(++testerCount)
        tester.assertValueAt(0) { it.isEmpty() }

        val pdfScore = pdfScore().copy(authorId = 0L)

        assertThatThrownBy { tested.insert(pdfScore) }
            .isInstanceOf(SQLiteConstraintException::class.java)
    }

    @Test
    fun insertsPdfScore() {
        val tester = tested.getAll().test()
        tester.awaitCount(++testerCount)
        tester.assertValueAt(testerCount - 1) { it.isEmpty() }

        val author = givenDbContainsAuthor()
        val pdfScore = pdfScore().copy(authorId = author.id)
        val pdfScoreId = tested.insert(pdfScore)[0]

        tester.awaitCount(++testerCount)
        tester.assertValueAt(testerCount - 1) {
            it.size == 1 && it[0] == pdfScore.copy(id = pdfScoreId)
        }
    }

    @Test
    fun deletesPdfScoresWhenDeletingAssociateAuthor() {
        val tester = tested.getAll().test()
        tester.awaitCount(++testerCount)
        tester.assertValueAt(testerCount - 1) { it.isEmpty() }
        val nAuthors = 5
        val nPdfScoresPerAuthor = 5
        val authors = (0 until nAuthors).map { givenDbContainsAuthor(author(it)) }
        val pdfScores = mutableListOf<PdfScore>()
        authors.forEach { author ->
            val pdfScoresForAuthor = (0 until nPdfScoresPerAuthor).map {
                pdfScore(it).copy(authorId = author.id)
            }
            val pdfScoreIds = tested.insert(*pdfScoresForAuthor.toTypedArray())
            pdfScoresForAuthor.mapIndexed { index, pdfScore ->
                pdfScore.copy(id = pdfScoreIds[index])
            }
            pdfScores.addAll(pdfScoresForAuthor)
            tester.awaitCount(++testerCount)
        }
        tester.assertValueAt(testerCount - 1) { it.size == nAuthors * nPdfScoresPerAuthor }

        authorsDao.delete(authors[0].id)

        tester.awaitCount(++testerCount)
        tester.assertValueAt(testerCount - 1) { scores ->
            scores.size == (nAuthors - 1) * nPdfScoresPerAuthor && !scores.any { it.authorId == authors[0].id }
        }
    }

    private fun givenDbContainsAuthor(author: Author = author()): Author = authorsDao
        .insert(listOf(author))
        .let { author.copy(id = it[0]) }
}