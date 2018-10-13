package com.lelloman.launcher.persistence.db.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.lelloman.common.utils.model.ModelWithId
import com.lelloman.launcher.persistence.db.AppDatabase.Companion.CLASSIFIED_IDENTIFIER_TABLE_NAME

@Entity(tableName = CLASSIFIED_IDENTIFIER_TABLE_NAME)
data class ClassifiedIdentifier(
    @PrimaryKey(autoGenerate = true) override val id: Long = 0L,
    val identifier: String,
    val score: Double
) : ModelWithId