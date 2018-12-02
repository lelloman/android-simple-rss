package com.lelloman.pdfscores

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry.getTargetContext
import com.lelloman.pdfscores.persistence.AppDatabase
import com.lelloman.pdfscores.persistence.PdfScoresDao
import org.junit.Before

class PdfScoresDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var tested: PdfScoresDao

    @Before
    fun setUp() {
        val app = getTargetContext().applicationContext as PdfScoresApplication
        db = Room.inMemoryDatabaseBuilder(app, AppDatabase::class.java).build()
        tested = db.pdfScoresDao()
    }
}