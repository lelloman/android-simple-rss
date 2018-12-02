package com.lelloman.pdfscores.persistence

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.lelloman.common.utils.model.ModelWithId
import com.lelloman.pdfscores.persistence.AppDatabase.Companion.AUTHORS_TABLE_NAME

@Entity(
    tableName = AUTHORS_TABLE_NAME
)
data class Author(
    @PrimaryKey(autoGenerate = true) override val id: Long = 0L,
    val firstName: String,
    val lastName: String
) : ModelWithId