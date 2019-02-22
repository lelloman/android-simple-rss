package com.lelloman.simplerss.persistence.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lelloman.simplerss.persistence.db.AppDatabase.Companion.ARTICLE_TABLE_NAME
import com.lelloman.simplerss.persistence.db.AppDatabase.Companion.SOURCE_TABLE_NAME
import com.lelloman.simplerss.persistence.db.model.Article
import com.lelloman.simplerss.persistence.db.model.SourceArticle
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