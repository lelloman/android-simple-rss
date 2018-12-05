package com.lelloman.pdfscores.persistence

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.ForeignKey.CASCADE
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey
import com.lelloman.common.utils.model.ModelWithId
import com.lelloman.pdfscores.persistence.AppDatabase.Companion.PDF_SCORES_TABLE_NAME
import com.lelloman.pdfscores.persistence.PdfScore.Companion.COLUMN_AUTHOR_ID

@Entity(
    tableName = PDF_SCORES_TABLE_NAME,
    foreignKeys = [ForeignKey(
        entity = Author::class,
        parentColumns = ["id"],
        childColumns = [COLUMN_AUTHOR_ID],
        onDelete = CASCADE
    )
    ],
    indices = [Index(COLUMN_AUTHOR_ID)]
)
data class PdfScore(
    @PrimaryKey(autoGenerate = true) override val id: Long,
    val fileName: String,
    val created: Long,
    val lastOpened: Long,
    val title: String,
    val authorId: Long
) : ModelWithId {

    companion object {
        const val COLUMN_AUTHOR_ID = "authorId"
    }
}