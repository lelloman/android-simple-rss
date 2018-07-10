package com.lelloman.read.core

import com.google.common.truth.Truth.assertThat
import com.lelloman.read.R
import com.lelloman.read.testutils.MockResourceProvider
import com.lelloman.read.testutils.dummySource
import com.nhaarman.mockito_kotlin.mock
import org.junit.Test
import java.util.*

class SemanticTimeProviderTest {

    private var now = 123456789123L

    private val timeProvider: TimeProvider = mock {
        on { nowUtcMs() }.thenAnswer { now }
    }

    private val myTimestamp = 527505620_000L

    val tested = SemanticTimeProvider(
        timeProvider = timeProvider,
        resourceProvider = MockResourceProvider(),
        givenTimeZone = TimeZone.getTimeZone("UTC")
    )

    @Test
    fun `returns formatted time utc`() {
        val tested = SemanticTimeProvider(
            timeProvider = timeProvider,
            resourceProvider = MockResourceProvider(),
            givenTimeZone = TimeZone.getTimeZone("UTC")
        )

        val formatted = tested.getDateTimeString(myTimestamp)

        assertThat(formatted).isEqualTo("1986-09-19 09:20")
    }

    @Test
    fun `returns formatted time europe rome`() {
        val tested = SemanticTimeProvider(
            timeProvider = timeProvider,
            resourceProvider = MockResourceProvider(),
            givenTimeZone = TimeZone.getTimeZone("Europe/Rome")
        )

        val formatted = tested.getDateTimeString(myTimestamp)

        assertThat(formatted).isEqualTo("1986-09-19 11:20")
    }

    @Test
    fun `returns source last fetched negative time`() {
        val source = dummySource().copy(lastFetched = -10_000)

        val lastFetched = tested.getSourceLastFetchedString(source)

        assertThat(lastFetched).isEqualTo("${R.string.last_refresh}:${R.string.never}")
    }

    @Test
    fun `returns source last fetched now`() {
        val source = dummySource().copy(lastFetched = now)

        val lastFetched = tested.getSourceLastFetchedString(source)

        assertThat(lastFetched).isEqualTo("${R.string.last_refresh}:${R.string.time_diff_seconds}:0")
    }

    @Test
    fun `returns source last fetched 10 sec ago`() {
        val source = dummySource().copy(lastFetched = now - 10_000)

        val lastFetched = tested.getSourceLastFetchedString(source)

        assertThat(lastFetched).isEqualTo("${R.string.last_refresh}:${R.string.time_diff_seconds}:10")
    }

    @Test
    fun `returns source last fetched 5 min and 10 sec ago`() {
        val delta = 60_000 * 5 + 10_000
        val source = dummySource().copy(lastFetched = now - delta)

        val lastFetched = tested.getSourceLastFetchedString(source)

        assertThat(lastFetched).isEqualTo("${R.string.last_refresh}:${R.string.time_diff_minutes}:5")
    }

    @Test
    fun `returns source last fetched 2 hours 5 min and 10 sec ago`() {
        val delta = 2 * 60_000 * 60 + 60_000 * 5 + 10_000
        val source = dummySource().copy(lastFetched = now - delta)

        val lastFetched = tested.getSourceLastFetchedString(source)

        assertThat(lastFetched).isEqualTo("${R.string.last_refresh}:${R.string.time_diff_hours}:2")
    }

    @Test
    fun `returns source last fetched 7 days 2 hours 5 min and 10 sec ago`() {
        val delta = 7 * (24 * 60_000 * 60) + 2 * 60_000 * 60 + 60_000 * 5 + 10_000
        val source = dummySource().copy(lastFetched = now - delta)

        val lastFetched = tested.getSourceLastFetchedString(source)

        assertThat(lastFetched).isEqualTo("${R.string.last_refresh}:${R.string.time_diff_days}:7")
    }

    @Test
    fun `returns time quantity 0`() {
        val quantity = tested.getTimeQuantity(0)

        assertThat(quantity).isEqualTo("${R.string.time_quantity_sec}:0")
    }

    @Test
    fun `returns 0 sec for negative quantity`() {
        val quantity = tested.getTimeQuantity(-1234)

        assertThat(quantity).isEqualTo("${R.string.time_quantity_sec}:0")
    }

    @Test
    fun `returns seconds only quantity`(){
        TODO("implement me")
    }

    @Test
    fun `returns seconds and minutes quantity`(){
        TODO("implement me")
    }

    @Test
    fun `returns seconds minutes and hours quantity`(){
        TODO("implement me")
    }

    @Test
    fun `returns seconds minutes hours and days quantity`(){
        TODO("implement me")
    }

    @Test
    fun `returns seconds and days quantity`(){
        TODO("implement me")
    }

    @Test
    fun `returns minutes and hours quantity`(){
        TODO("implement me")
    }
}