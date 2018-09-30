package com.lelloman.read.persistence.db.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.lelloman.common.utils.model.ModelWithId
import com.lelloman.read.utils.Constants.SOURCE_TABLE_NAME

@Suppress("ArrayInDataClass")
@Entity(tableName = SOURCE_TABLE_NAME)
data class Source(
    @PrimaryKey(autoGenerate = true) override val id: Long = 0L,
    val name: String,
    val url: String,
    var lastFetched: Long = 0L,
    val isActive: Boolean,
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB) var favicon: ByteArray? = null,
    val immutableHashCode: Int = name.hashCode() * url.hashCode()
) : ModelWithId