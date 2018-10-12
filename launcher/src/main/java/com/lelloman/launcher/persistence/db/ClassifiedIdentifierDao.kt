package com.lelloman.launcher.persistence.db

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.lelloman.launcher.persistence.db.model.ClassifiedIdentifier
import io.reactivex.Flowable

@Dao
interface ClassifiedIdentifierDao {

    @Query("SELECT * FROM ${AppDatabase.CLASSIFIED_IDENTIFIER_TABLE_NAME}")
    fun getAll(): Flowable<List<ClassifiedIdentifier>>

    @Query("DELETE FROM ${AppDatabase.CLASSIFIED_IDENTIFIER_TABLE_NAME}")
    fun deleteAll()

    @Insert
    fun insert(classifiedIdentifiers: List<ClassifiedIdentifier>)
}