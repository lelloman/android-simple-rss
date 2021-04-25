package com.lelloman.simplerss.domain_local_sources.internal.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lelloman.simplerss.domain_local_sources.LocalSourceItem
import com.lelloman.simplerss.domain_local_sources.internal.db.LocalSourceItemEntity.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class LocalSourceItemEntity(

    @PrimaryKey(autoGenerate = true)
    override val id: Long,

    @ColumnInfo(name = COLUMN_SOURCE_ID)
    val localSourceId: Long,

    override val title: String,

    override val subtitle: String,

    override val content: String,

    override val link: String,

    override val imageUrl: String?,

    override val time: Long,

    ) : LocalSourceItem {
    companion object {
        const val TABLE_NAME = "LocalSourceItem"

        const val COLUMN_SOURCE_ID = "SourceId"
    }
}