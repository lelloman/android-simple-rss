package com.lelloman.pdfscores

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry.getTargetContext
import com.google.common.truth.Truth.assertThat
import com.lelloman.pdfscores.persistence.db.AppDatabase
import com.lelloman.pdfscores.persistence.db.AuthorsDao
import com.lelloman.pdfscores.persistence.model.Author
import org.junit.Before
import org.junit.Test

class AuthorsDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var tested: AuthorsDao

    private var testerCount = 0

    @Before
    fun setUp() {
        val app = getTargetContext().applicationContext as PdfScoresApplication
        db = Room.inMemoryDatabaseBuilder(app, AppDatabase::class.java).build()
        tested = db.authorsDao()
        testerCount = 0
    }

    @Test
    fun returnsEmptyListWhenCreated() {
        val tester = tested.getAll().test()

        tester.awaitCount(1)

        tester.apply {
            assertNotComplete()
            assertValueCount(1)
            assertValueAt(0) { it.isEmpty() }
        }
    }

    @Test
    fun addsOneAuthor() {
        val author = Author(firstName = "MEEEEOW", lastName = "WOOF")

        val authorId = tested.insert(author)[0]
        val expectedAuthor = author.copy(id = authorId)

        val actualAuthors = tested.getAll().blockingFirst()
        assertThat(actualAuthors).hasSize(1)
        assertThat(actualAuthors[0]).isEqualTo(expectedAuthor)
    }

    @Test
    fun addsAndDeletes2Authors() {
        val tester = tested.getAll().test()
        tester.awaitCount(++testerCount)
        tester.assertValueAt(testerCount - 1) { it.isEmpty() }

        val author1 = Author(firstName = "BLBLBL", lastName = "ZZZ Last lastName")
        val author2 = Author(firstName = "MR", lastName = "AAA First lastName")
        val (authorId1, authorId2) = tested.insert(author1, author2)

        tester.awaitCount(++testerCount)
        tester.assertValueAt(testerCount - 1) {
            assertThat(it[0]).isEqualTo(author2.copy(id = authorId2))
            assertThat(it[1]).isEqualTo(author1.copy(id = authorId1))
            it.size == 2
        }

        tested.delete(authorId1)

        tester.awaitCount(++testerCount)
        tester.assertValueAt(testerCount - 1) {
            it.size == 1
        }

        tested.delete(authorId2)

        tester.awaitCount(++testerCount)
        tester.assertValueAt(testerCount - 1) {
            it.isEmpty()
        }
    }

    @Test
    fun deletesListOfAuthorsAndPushesOnlyOneUpdate() {
        val tester = tested.getAll().test()
        tester.awaitCount(++testerCount)
        tester.assertValueAt(0) { it.isEmpty() }

        val n = 10
        val authorIds = (0 until n)
            .map(::author)
            .toTypedArray()
            .let(tested::insert)

        tester.awaitCount(++testerCount)
        tester.assertValueAt(testerCount - 1) {
            it.size == n
        }

        tested.delete(*authorIds.toLongArray())

        tester.awaitCount(++testerCount)
        tester.assertValueAt(testerCount - 1) {
            it.isEmpty()
        }
    }

    @Test
    fun updatesOneAuthor() {
        val tester = tested.getAll().test()
        tester.awaitCount(++testerCount)
        tester.assertValueAt(testerCount - 1) { it.isEmpty() }

        val author = Author(firstName = "Alfredo", lastName = "Canale")
        val authorId = tested.insert(author)[0]

        tester.awaitCount(++testerCount)
        tester.assertValueAt(testerCount - 1) {
            it.size == 1 && it[0] == author.copy(id = authorId)
        }

        val updatedAuthor = author.copy(id = authorId, firstName = "Not Alfredo")
        tested.update(updatedAuthor)

        tester.awaitCount(++testerCount)
        tester.assertValueAt(testerCount - 1) {
            it.size == 1 && it[0] == updatedAuthor
        }
    }
}