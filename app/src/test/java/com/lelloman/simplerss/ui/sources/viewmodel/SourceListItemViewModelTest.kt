package com.lelloman.simplerss.ui.sources.viewmodel

import com.google.common.truth.Truth.assertThat
import com.lelloman.common.jvmtestutils.MockResourceProvider
import com.lelloman.common.view.SemanticTimeProvider
import com.lelloman.simplerss.R
import com.lelloman.simplerss.persistence.db.model.Source
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Test

class SourceListItemViewModelTest {

    private val resourceProvider = MockResourceProvider()
    private val semanticTimeProvider: SemanticTimeProvider = mock()
    private val onIsActiveChanged: (Boolean) -> Unit = mock()

    private val tested = SourceListItemViewModel(
        resourceProvider = resourceProvider,
        semanticTimeProvider = semanticTimeProvider,
        onIsActiveChanged = onIsActiveChanged
    )

    @Test
    fun `calls on is active changed callback`() {
        tested.onIsActiveChanged(true)
        verify(onIsActiveChanged).invoke(true)

        tested.onIsActiveChanged(false)
        verify(onIsActiveChanged).invoke(false)
    }

    @Test
    fun `binds data from source`() {
        val source = Source(
            name = "the name",
            url = "www.mipuzzanoipiedi.it",
            isActive = true,
            lastFetched = 1L
        )
        val lastFetchedString = "mipuzzanoipiediperdavver"
        whenever(semanticTimeProvider.getTimeDiffString(any())).thenReturn(lastFetchedString)

        tested.bind(source)

        tested.apply {
            assertThat(name).isEqualTo(source.name)
            assertThat(url).isEqualTo(source.url)
            assertThat(hash).isEqualTo(source.immutableHashCode)
            assertThat(lastFetched.get()).isEqualTo("${R.string.last_refresh}:$lastFetchedString")
            assertThat(isActive).isEqualTo(source.isActive)
        }
    }
}