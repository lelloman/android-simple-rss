package com.lelloman.read.persistence.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.lelloman.read.core.ModelWithId

@Entity
data class Article(
    @PrimaryKey override val id: Long,
    val title: String,
    val subtitle: String,
    val time: Long,
    val sourceName: String,
    val sourceId: Long
) : ModelWithId