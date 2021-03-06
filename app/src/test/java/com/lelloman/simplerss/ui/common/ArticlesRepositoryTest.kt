package com.lelloman.simplerss.ui.common

import com.lelloman.simplerss.feed.FeedRefresher
import com.lelloman.simplerss.persistence.db.ArticlesDao
import com.lelloman.simplerss.ui.common.repository.ArticlesRepository
import com.nhaarman.mockitokotlin2.anyVararg
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Flowable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import org.junit.Test

class ArticlesRepositoryTest {

    private val articlesDao: ArticlesDao = mock {
        on { getAllFromActiveSources() }.thenReturn(Flowable.just(emptyList()))
        on { insertAll(anyVararg()) }.thenReturn(emptyList())
    }
    private val feedRefresherIsLoadingSubject: Subject<Boolean> = PublishSubject.create()
    private val feedRefresher: FeedRefresher = mock {
        on { isLoading }.thenReturn(feedRefresherIsLoadingSubject.hide())
    }

    private val tested = ArticlesRepository(
        articlesDao = articlesDao,
        feedRefresher = feedRefresher
    )

    @Test
    fun `loading stream returns feed refresher events`() {
        val tester = tested.loading.test()

        tester.assertNoValues()

        feedRefresherIsLoadingSubject.onNext(true)
        tester.assertValues(true)

        feedRefresherIsLoadingSubject.onNext(false)
        tester.assertValues(true, false)
    }

    @Test
    fun `does not emit same is loading event as the previous one`() {
        val tester = tested.loading.test()

        feedRefresherIsLoadingSubject.onNext(true)

        tester.assertValues(true)

        feedRefresherIsLoadingSubject.onNext(true)

        tester.assertValues(true)

        feedRefresherIsLoadingSubject.onNext(false)

        tester.assertValues(true, false)
    }

    @Test
    fun `fetches articles from all active source through dao`() {
        whenever(articlesDao.getAllFromActiveSources()).thenReturn(Flowable.just(SOURCE_ARTICLES))

        val tester = tested.fetchArticles().test()

        tester.assertValues(SOURCE_ARTICLES)
        verify(articlesDao).getAllFromActiveSources()
    }

    @Test
    fun `inserts articles in dao`() {
        tested.insertArticles(ARTICLES).test()

        verify(articlesDao).insertAll(anyVararg())
    }

    private companion object {
        val ARTICLES = listOf<com.lelloman.simplerss.persistence.db.model.Article>(mock(), mock())
        val SOURCE_ARTICLES = listOf<com.lelloman.simplerss.persistence.db.model.SourceArticle>(mock(), mock())
    }
}