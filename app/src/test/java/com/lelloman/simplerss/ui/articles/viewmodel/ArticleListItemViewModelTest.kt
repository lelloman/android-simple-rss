package com.lelloman.simplerss.ui.articles.viewmodel

import androidx.lifecycle.Lifecycle
import com.google.common.truth.Truth.assertThat
import com.lelloman.common.view.SemanticTimeProvider
import com.lelloman.simplerss.mock.MockAppSettings
import com.lelloman.simplerss.persistence.db.model.SourceArticle
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.schedulers.Schedulers.trampoline
import io.reactivex.subjects.BehaviorSubject
import org.junit.Test

class ArticleListItemViewModelTest {

    private val imagesEnabledSubject = BehaviorSubject.create<Boolean>()

    private val appSettings = MockAppSettings(
        providedArticleListImagesEnabled = imagesEnabledSubject
    )
    private val lifecycle: Lifecycle = mock()
    private val semanticTimeProvider: SemanticTimeProvider = mock()

    private val tested = ArticleListItemViewModel(
        lifecycle = lifecycle,
        uiScheduler = trampoline(),
        appSettings = appSettings,
        semanticTimeProvider = semanticTimeProvider
    )

    @Test
    fun `subscribes on instantiation and un-subscribes on destroy`() {
        assertThat(imagesEnabledSubject.hasObservers()).isTrue()

        tested.onDestroy()

        assertThat(imagesEnabledSubject.hasObservers()).isFalse()
    }

    @Test
    fun `updates images enabled values when settings is set to true`() {
        tested.bind(ARTICLE)

        imagesEnabledSubject.onNext(true)

        assertThat(tested.imageVisible).isTrue()
        assertThat(tested.imageUrl).isEqualTo(ARTICLE.imageUrl)
    }

    @Test
    fun `updates images enabled values when settings is set to false`() {
        tested.bind(ARTICLE)

        imagesEnabledSubject.onNext(false)

        assertThat(tested.imageVisible).isFalse()
        assertThat(tested.imageUrl).isNull()
    }

    @Test
    fun `binds data from article with images enabled`() {
        imagesEnabledSubject.onNext(true)
        val dateTimeString = "(o)_(o)"
        whenever(semanticTimeProvider.getDateTimeString(any())).thenReturn(dateTimeString)

        tested.bind(ARTICLE)

        tested.apply {
            assertThat(title).isEqualTo(ARTICLE.title)
            assertThat(details).isEqualTo("$dateTimeString - ${ARTICLE.sourceName}")
            assertThat(hash).isEqualTo(ARTICLE.hashCode())
            assertThat(subtitle).isEqualTo(ARTICLE.subtitle)
            assertThat(subtitleVisible).isTrue()
            assertThat(imageUrl).isEqualTo(ARTICLE.imageUrl)
            assertThat(imageVisible).isTrue()
        }
    }

    @Test
    fun `binds data from article with images disabled`() {
        imagesEnabledSubject.onNext(false)

        tested.bind(ARTICLE)

        tested.apply {
            assertThat(imageUrl).isNull()
            assertThat(imageVisible).isFalse()
        }
    }

    @Test
    fun `binds data from article that has no image`() {
        imagesEnabledSubject.onNext(true)

        tested.bind(ARTICLE.copy(imageUrl = null))

        tested.apply {
            assertThat(imageUrl).isNull()
            assertThat(imageVisible).isFalse()
        }
    }

    private companion object {
        val ARTICLE = SourceArticle(
            id = 123,
            title = "the article",
            subtitle = "the subtitle",
            content = "the content",
            link = "the link",
            imageUrl = "antonio",
            time = 321,
            sourceId = 456,
            sourceName = "the sourceName",
            favicon = null
        )
    }
}