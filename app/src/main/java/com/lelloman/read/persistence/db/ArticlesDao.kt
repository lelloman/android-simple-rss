package com.lelloman.read.persistence.db

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.lelloman.read.persistence.db.model.Article
import com.lelloman.read.utils.Constants.ARTICLE_TABLE_NAME
import io.reactivex.Flowable

@Dao
interface ArticlesDao {

    @Query("SELECT * from $ARTICLE_TABLE_NAME ORDER BY time DESC")
    fun getAll(): Flowable<List<Article>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg article: Article): List<Long>

    @Query("DELETE from $ARTICLE_TABLE_NAME WHERE sourceId = :sourceId")
    fun deleteArticlesFromSource(sourceId: Long)
}