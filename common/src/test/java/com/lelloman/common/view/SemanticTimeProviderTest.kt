package com.lelloman.common.view

import com.lelloman.common.R
import com.lelloman.common.testutils.MockResourceProvider
import com.lelloman.common.utils.TimeProvider
import com.lelloman.common.utils.model.DayTime
import com.lelloman.common.utils.model.Time
import com.lelloman.common.utils.model.WeekTime
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.util.*

class SemanticTimeProviderTest {

    private var now = 123456789123L

    private val timeProvider = object : TimeProvider {
        override fun nowUtcMs() = now
        override fun getTime(utcMs: Long) = Time(
            weekTime = WeekTime(0, 0),
            dayTime = DayTime(0, 0)
        )

        override fun now() = getTime(nowUtcMs())
    }

    private val myTimestamp = 527505620_000L

    val tested = SemanticTimeProviderImpl(
        timeProvider = timeProvider,
        resourceProvider = MockResourceProvider().apply {
            registerStringArray(R.array.time_quantities, TIME_QUANTITIES)
        },
        givenTimeZone = TimeZone.getTimeZone("UTC")
    )

    @Test
    fun `returns formatted time utc`() {
        val tested = SemanticTimeProviderImpl(
            timeProvider = timeProvider,
            resourceProvider = MockResourceProvider(),
            givenTimeZone = TimeZone.getTimeZone("UTC")
        )

        val formatted = tested.getDateTimeString(myTimestamp)

        assertThat(formatted).isEqualTo("1986-09-19 09:20")
    }

    @Test
    fun `returns formatted time europe rome`() {
        val tested = SemanticTimeProviderImpl(
            timeProvider = timeProvider,
            resourceProvider = MockResourceProvider(),
            givenTimeZone = TimeZone.getTimeZone("Europe/Rome")
        )

        val formatted = tested.getDateTimeString(myTimestamp)

        assertThat(formatted).isEqualTo("1986-09-19 11:20")
    }

    @Test
    fun `returns source last fetched now`() {
        val lastFetched = now

        val timeDiff = tested.getTimeDiffString(lastFetched)

        assertThat(timeDiff).isEqualTo("${R.string.time_diff_seconds}:0")
    }

    @Test
    fun `returns source last fetched 10 sec ago`() {
        val lastFetched = now - 10_000

        val timeDiff = tested.getTimeDiffString(lastFetched)

        assertThat(timeDiff).isEqualTo("${R.string.time_diff_seconds}:10")
    }

    @Test
    fun `returns source last fetched 5 min and 10 sec ago`() {
        val delta = 60_000 * 5 + 10_000
        val lastFetched = now - delta

        val timeDiff = tested.getTimeDiffString(lastFetched)

        assertThat(timeDiff).isEqualTo("${R.string.time_diff_minutes}:5")
    }

    @Test
    fun `returns source last fetched 2 hours 5 min and 10 sec ago`() {
        val delta = 2 * 60_000 * 60 + 60_000 * 5 + 10_000
        val lastFetched = now - delta

        val timeDiff = tested.getTimeDiffString(lastFetched)

        assertThat(timeDiff).isEqualTo("${R.string.time_diff_hours}:2")
    }

    @Test
    fun `returns source last fetched 7 days 2 hours 5 min and 10 sec ago`() {
        val delta = 7 * (24 * 60_000 * 60) + 2 * 60_000 * 60 + 60_000 * 5 + 10_000
        val lastFetched = now - delta

        val timeDiff = tested.getTimeDiffString(lastFetched)

        assertThat(timeDiff).isEqualTo("${R.string.time_diff_days}:7")
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
    fun `returns seconds only quantity`() {
        val quantity = tested.getTimeQuantity(30_000)

        assertThat(quantity).isEqualTo(SEC.format(30))
    }

    @Test
    fun `returns seconds and minutes quantity`() {
        val expected = "${SEC.format(30)} ${MIN.format(5)}"

        val quantity = tested.getTimeQuantity(30_000 + 5 * 60_000)

        assertThat(quantity).isEqualTo(expected)
    }

    @Test
    fun `returns seconds minutes and hours quantity`() {
        val expected = "${SEC.format(59)} ${MIN.format(59)} ${HOURS.format(3)}"

        val quantity = tested.getTimeQuantity(59_000 + 59 * 60_000 + 3 * 60 * 60_000)

        assertThat(quantity).isEqualTo(expected)
    }

    @Test
    fun `returns seconds minutes hours and days quantity`() {
        val expected = "${SEC.format(59)} ${MIN.format(59)} ${HOURS.format(23)} ${DAYS.format(2)}"

        val quantity = tested.getTimeQuantity(59_000 + 59 * 60_000 + 23 * 60 * 60_000 + 2 * 24 * 60 * 60_000)

        assertThat(quantity).isEqualTo(expected)
    }

    @Test
    fun `returns seconds and days quantity`() {
        val expected = "${SEC.format(59)} ${DAYS.format(2)}"

        val quantity = tested.getTimeQuantity(59_000 + 2 * 24 * 60 * 60_000L)

        assertThat(quantity).isEqualTo(expected)
    }

    @Test
    fun `returns minutes and hours quantity`() {
        val expected = "${MIN.format(59)} ${HOURS.format(1)}"

        val quantity = tested.getTimeQuantity(59 * 60_000L + 1 * 60 * 60_000L)

        assertThat(quantity).isEqualTo(expected)
    }

    private companion object {
        const val SEC = "%ss"
        const val MIN = "%sm"
        const val HOURS = "%sh"
        const val DAYS = "%sd"

        val TIME_QUANTITIES = arrayOf(SEC, MIN, HOURS, DAYS)
    }
}