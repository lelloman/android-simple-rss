package com.lelloman.read.ui.sources.viewmodel

import com.google.common.truth.Truth.assertThat
import com.lelloman.common.view.ResourceProvider
import com.lelloman.common.view.SemanticTimeProvider
import com.lelloman.read.persistence.db.model.Source
import com.lelloman.read.testutils.AndroidArchTest
import com.lelloman.read.ui.common.repository.SourcesRepository
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers.trampoline
import org.junit.Test


class SourceViewModelImplTest : AndroidArchTest() {

    private val semanticTimeProvider: SemanticTimeProvider = mock {
        on { getTimeDiffString(any()) }.thenReturn(LAST_FETCHED_STRING)
    }
    private val resourceProvider: ResourceProvider = mock()
    private val sourcesRepository: SourcesRepository = mock {
        on { getSource(SOURCE_1.id) }.thenReturn(Flowable.just(SOURCE_1))
        on { getSource(SOURCE_2.id) }.thenReturn(Flowable.just(SOURCE_2))
    }

    private lateinit var tested: SourceViewModelImpl

    override fun setUp() {
        tested = SourceViewModelImpl(
            ioScheduler = trampoline(),
            uiScheduler = trampoline(),
            semanticTimeProvider = semanticTimeProvider,
            sourcesRepository = sourcesRepository,
            resourceProvider = resourceProvider
        )
    }

    @Test
    fun `all values are empty before loading source id`() {
        assertThat(tested.sourceName.value).isNull()
        assertThat(tested.sourceLastFetched.value).isNull()
        assertThat(tested.sourceUrl.value).isNull()
    }

    @Test
    fun `updates values when source id is loaded`() {
        tested.onSourceIdLoaded(SOURCE_1.id)

        assertThat(tested.sourceName.value).isEqualTo(SOURCE_1.name)
//        assertThat(tested.sourceLastFetched.value).isEqualTo(LAST_FETCHED_STRING)
        assertThat(tested.sourceUrl.value).isEqualTo(SOURCE_1.url)
    }

    @Test
    fun `does not load source from db if same id is loaded twice`() {
        tested.onSourceIdLoaded(SOURCE_1.id)
        verify(sourcesRepository, times(1)).getSource(SOURCE_1.id)

        tested.onSourceIdLoaded(SOURCE_1.id)
        verify(sourcesRepository, times(1)).getSource(SOURCE_1.id)
    }

    @Test
    fun `reloads source if loaded id is different`() {
        tested.onSourceIdLoaded(SOURCE_1.id)
        verify(sourcesRepository).getSource(SOURCE_1.id)
        tested.onSourceIdLoaded(SOURCE_2.id)

        verify(sourcesRepository).getSource(SOURCE_2.id)
        assertThat(tested.sourceName.value).isEqualTo(SOURCE_2.name)
        assertThat(tested.sourceUrl.value).isEqualTo(SOURCE_2.url)
    }

    private companion object {
        val SOURCE_1 = Source(
            id = 1L,
            name = "source 1",
            url = "url 1",
            lastFetched = 111L,
            isActive = true
        )

        val SOURCE_2 = Source(
            id = 2L,
            name = "source 2",
            url = "url 2",
            lastFetched = 222L,
            isActive = true
        )

        const val LAST_FETCHED_STRING = "meeeow"
    }
}