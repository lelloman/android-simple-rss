package com.lelloman.read.unit

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.lelloman.read.ReadApplication
import com.lelloman.read.persistence.db.AppDatabase
import com.lelloman.read.persistence.db.ArticlesDao
import com.lelloman.read.persistence.db.model.Article
import com.lelloman.read.persistence.db.model.Source
import com.lelloman.read.persistence.db.model.SourceArticle
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class ArticlesDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var tested: ArticlesDao

    @Before
    fun setUp() {
        val app = InstrumentationRegistry.getTargetContext().applicationContext as ReadApplication
        db = Room.inMemoryDatabaseBuilder(app, AppDatabase::class.java).build()
        tested = db.articlesDao()
    }

    @Test
    fun returnsArticlesFromActiveSources() {
        givenSources1And2AreInserted()

        val articles = tested.getAllFromActiveSources().blockingFirst()

        assertEquals(articles[0], sourceArticle(ARTICLE_1_1, SOURCE_1))
        assertEquals(articles[1], sourceArticle(ARTICLE_2_1, SOURCE_2))
        assertEquals(articles[2], sourceArticle(ARTICLE_2_2, SOURCE_2))
        assertEquals(articles[3], sourceArticle(ARTICLE_1_2, SOURCE_1))
    }

    private fun givenSources1And2AreInserted() {
        db.sourcesDao().let { sourcesDao ->
            sourcesDao.insert(SOURCE_1)
            sourcesDao.insert(SOURCE_2)
        }
        db.articlesDao().insertAll(
            ARTICLE_1_1,
            ARTICLE_1_2,
            ARTICLE_2_1,
            ARTICLE_2_2
        )
    }

    private companion object {
        val SOURCE_1 = Source(
            id = 1,
            name = "source 1",
            url = "www.1",
            lastFetched = 0L,
            isActive = true
        )

        val SOURCE_2 = Source(
            id = 2,
            name = "source 2",
            url = "www.2",
            lastFetched = 0L,
            isActive = true
        )

        val ARTICLE_1_1 = dummyArticle2(index = 1).copy(sourceId = 1, time = 4)
        val ARTICLE_1_2 = dummyArticle2(index = 2).copy(sourceId = 1, time = 1)
        val ARTICLE_2_1 = dummyArticle2(index = 3).copy(sourceId = 2, time = 3)
        val ARTICLE_2_2 = dummyArticle2(index = 4).copy(sourceId = 2, time = 2)
    }
}

fun dummyArticle2(index: Int = 1) = Article(
    id = index.toLong(),
    title = "article $index",
    subtitle = "subtitle $index",
    content = "content $index",
    link = "link $index",
    imageUrl = "image url $index",
    time = index.toLong(),
    sourceId = index.toLong()
)

fun sourceArticle(article: Article, source: Source) = SourceArticle(
    id = article.id,
    title = article.title,
    subtitle = article.subtitle,
    content = article.content,
    link = article.link,
    imageUrl = article.imageUrl,
    time = article.time,
    sourceId = source.id,
    sourceName = source.name,
    favicon = source.favicon
)