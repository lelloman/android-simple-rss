package com.lelloman.pdfscores.persistence.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.ForeignKey.CASCADE
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey
import com.lelloman.common.utils.model.ModelWithId
import com.lelloman.pdfscores.persistence.db.AppDatabase.Companion.PDF_SCORES_TABLE_NAME
import com.lelloman.pdfscores.persistence.model.PdfScoreModel.Companion.COLUMN_AUTHOR_ID

interface PdfScore : ModelWithId<String> {
    val uri: String
    val created: Long
    val lastOpened: Long
    val title: String
    val authorId: String
    val isAsset: Boolean
}

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
data class PdfScoreModel(
    @PrimaryKey override val id: String,
    override val uri: String,
    override val created: Long,
    override val lastOpened: Long,
    override val title: String,
    override val authorId: String,
    override val isAsset: Boolean
) : PdfScore {
    companion object {
        const val COLUMN_AUTHOR_ID = "authorId"
        const val COLUMN_LAST_OPENED = "lastOpened"
    }
}