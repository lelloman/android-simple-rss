package com.lelloman.read.persistence.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.lelloman.read.core.ModelWithId
import com.lelloman.read.utils.Constants.SOURCE_TABLE_NAME

@Entity(tableName = SOURCE_TABLE_NAME)
data class Source(
    @PrimaryKey(autoGenerate = true) override val id: Long,
    val name: String,
    val url: String,
    val lastFetched: Long
) : ModelWithId