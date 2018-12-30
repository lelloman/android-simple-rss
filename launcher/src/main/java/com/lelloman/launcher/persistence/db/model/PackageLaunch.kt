package com.lelloman.launcher.persistence.db.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.lelloman.common.utils.model.ModelWithId
import com.lelloman.launcher.persistence.db.AppDatabase.Companion.PACKAGE_LAUNCH_TABLE_NAME

@Entity(tableName = PACKAGE_LAUNCH_TABLE_NAME)
data class PackageLaunch(
    @PrimaryKey(autoGenerate = true) override val id: Long = 0L,
    val timestampUtc: Long,
    val packageName: String,
    val activityName: String
) : ModelWithId<Long> {
    fun identifier() = "$packageName/$activityName"
}