package com.lelloman.pdfscores.persistence

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.lelloman.common.utils.model.ModelWithId
import com.lelloman.pdfscores.persistence.AppDatabase.Companion.PDF_SCORES_TABLE_NAME

@Entity(
    tableName = PDF_SCORES_TABLE_NAME
)
data class PdfScore(
    @PrimaryKey(autoGenerate = true) override val id: Long,
    val fileName: String,
    val created: Long,
    val lastOpened: Long,
    val title: String
) : ModelWithId