package com.lelloman.read.persistence.db

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.lelloman.read.persistence.db.AppDatabase.Companion.ARTICLE_TABLE_NAME
import com.lelloman.read.persistence.db.AppDatabase.Companion.SOURCE_TABLE_NAME
import com.lelloman.read.persistence.db.model.Article
import com.lelloman.read.persistence.db.model.SourceArticle
import io.reactivex.Flowable

@Suppress("AndroidUnresolvedRoomSqlReference")
@Dao
interface ArticlesDao {

    @Query("""SELECT
                        article.*,
                        favicon,
                        name AS sourceName
                    FROM $ARTICLE_TABLE_NAME article
                    LEFT JOIN $SOURCE_TABLE_NAME source
                    ON article.sourceId = source.id
                    WHERE source.isActive = 1
                    ORDER BY time DESC""")
    fun getAllFromActiveSources(): Flowable<List<SourceArticle>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg article: Article): List<Long>

    @Query("DELETE FROM $ARTICLE_TABLE_NAME WHERE sourceId = :sourceId")
    fun deleteArticlesFromSource(sourceId: Long)

    @Query("SELECT * FROM $ARTICLE_TABLE_NAME WhERE sourceId = :sourceId")
    fun getAllFromSource(sourceId: Long): Flowable<List<Article>>
}