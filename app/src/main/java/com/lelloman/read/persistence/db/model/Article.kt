package com.lelloman.read.persistence.db.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.ForeignKey.CASCADE
import android.arch.persistence.room.PrimaryKey
import com.lelloman.read.core.ModelWithId
import com.lelloman.read.utils.Constants.ARTICLE_TABLE_NAME

@Entity(tableName = ARTICLE_TABLE_NAME, foreignKeys = [ForeignKey(
    entity = Source::class,
    parentColumns = ["id"],
    childColumns = ["sourceId"],
    onDelete = CASCADE
)])
data class Article(
    @PrimaryKey(autoGenerate = true) override val id: Long,
    val title: String,
    val subtitle: String,
    val content: String,
    val link: String,
    val imageUrl: String?,
    val time: Long,
    val sourceName: String,
    val sourceId: Long
) : ModelWithId