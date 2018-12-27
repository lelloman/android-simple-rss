package com.lelloman.pdfscores.persistence.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.lelloman.common.utils.model.ModelWithId
import com.lelloman.pdfscores.persistence.db.AppDatabase.Companion.AUTHORS_TABLE_NAME

@Entity(
    tableName = AUTHORS_TABLE_NAME
)
data class Author(
    @PrimaryKey(autoGenerate = true) override val id: Long = 0L,
    val firstName: String,
    val lastName: String,
    val isAsset: Boolean,
    val collectionId: String
) : ModelWithId {

    companion object {
        const val COLUMN_ID = "id"
        const val COLUMN_LAST_NAME = "lastName"
    }
}