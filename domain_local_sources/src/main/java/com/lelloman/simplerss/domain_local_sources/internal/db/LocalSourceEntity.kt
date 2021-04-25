package com.lelloman.simplerss.domain_local_sources.internal.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lelloman.simplerss.domain_local_sources.LocalSource
import com.lelloman.simplerss.domain_local_sources.internal.db.LocalSourceEntity.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
@Suppress("ArrayInDataClass")
internal data class LocalSourceEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = COLUMN_ID)
    override val id: Long,

    override val name: String,

    override val url: String,

    override val lastRefresh: Long,

    override val isActive: Boolean,

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    override val icon: ByteArray?,

    override val immutableHashCode: Int = name.hashCode() * url.hashCode(),
) : LocalSource {

    companion object {
        const val TABLE_NAME = "LocalSource"

        const val COLUMN_ID = "id"
    }
}