package com.lelloman.launcher.classification

import com.lelloman.common.utils.model.WeekTime
import com.lelloman.common.utils.model.WeekTime.Companion.MONDAY
import com.lelloman.common.utils.model.WeekTime.Companion.SATURDAY
import com.lelloman.common.utils.model.WeekTime.Companion.SUNDAY
import com.lelloman.common.utils.model.WeekTime.Companion.THURSDAY
import com.lelloman.common.utils.model.WeekTime.Companion.TUESDAY
import com.lelloman.common.utils.model.WeekTime.Companion.WEDNESDAY
import org.assertj.core.api.AbstractDoubleArrayAssert
import org.assertj.core.api.AbstractDoubleAssert
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.data.Offset
import org.junit.Test

class TimeEncoderTest {

    private val tested = TimeEncoder()

    @Test
    fun `makes scalar time 1`() {
        val time = WeekTime(
            dayOfWeek = SUNDAY,
            hourOfDay = 0
        )

        val scalar = tested.makeScalarWeekTime(time)

        assertThat(scalar).isEqualTo(0)
    }

    @Test
    fun `makes scalar time 2`() {
        val time = WeekTime(
            dayOfWeek = MONDAY,
            hourOfDay = 0
        )

        val scalar = tested.makeScalarWeekTime(time)

        assertThat(scalar).isEqualTo(24)
    }

    @Test
    fun `makes scalar time 3`() {
        val time = WeekTime(
            dayOfWeek = WEDNESDAY,
            hourOfDay = 23
        )

        val scalar = tested.makeScalarWeekTime(time)

        assertThat(scalar).isEqualTo(24 + 24 + 24 + 23)
    }

    @Test
    fun `makes scalar time 4`() {
        val time = WeekTime(
            dayOfWeek = SATURDAY,
            hourOfDay = 15
        )

        val scalar = tested.makeScalarWeekTime(time)

        assertThat(scalar).isEqualTo(24 * 6 + 15)
    }

    @Test
    fun `sum of encoded time is always 1 and also pretty print`() {
        fun DoubleArray.fancyPrint() = map { String.format("%.2f", it) }
            .joinToString(",") { it }

        tested {
            for (day in 1..7) {
                for (hour in 0 until 24) {
                    val time = WeekTime(dayOfWeek = day, hourOfDay = hour)

                    val encoded = encodeWeekTime(time)

                    val hourString = String.format("%02d", hour)
                    println("$day-$hourString\t${encoded.fancyPrint()}")
                    assertThat(encoded.sum()).isDoubleEqualTo(1.0)
                }
            }
        }
    }

    @Test
    fun `encodes SUNDAY at midnight`() {
        val time = WeekTime(dayOfWeek = SUNDAY, hourOfDay = 0)

        val encoded = tested.encodeWeekTime(time)

        assertThat(encoded).isRoughlyEqualTo(
            doubleArrayOf(0.5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.5)
        )
    }

    @Test
    fun `encodes TUESDAY at noon`() {
        val time = WeekTime(dayOfWeek = TUESDAY, hourOfDay = 12)

        val encoded = tested.encodeWeekTime(time)

        assertThat(encoded).isRoughlyEqualTo(
            doubleArrayOf(0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0)
        )
    }

    @Test
    fun `encodes THURSDAY at 9 am`() {
        val time = WeekTime(dayOfWeek = THURSDAY, hourOfDay = 9)

        val encoded = tested.encodeWeekTime(time)

        assertThat(encoded).isRoughlyEqualTo(
            doubleArrayOf(0.0, 0.0, 0.0, 0.12, 0.88, 0.0, 0.0)
        )
    }

    @Test
    fun `encodes SATURDAY at 9 pm`() {
        val time = WeekTime(dayOfWeek = SATURDAY, hourOfDay = 21)

        val encoded = tested.encodeWeekTime(time)

        assertThat(encoded).isRoughlyEqualTo(
            doubleArrayOf(0.38, 0.0, 0.0, 0.0, 0.0, 0.0, 0.62)
        )
    }

    private fun tested(block: TimeEncoder.() -> Unit) =
        block.invoke(TimeEncoder())

    private fun AbstractDoubleAssert<*>.isDoubleEqualTo(expected: Double) {
        this.isCloseTo(expected, DOUBLE_OFFSET)
    }

    private fun AbstractDoubleArrayAssert<*>.isRoughlyEqualTo(expected: DoubleArray) {
        hasSize(expected.size)
        usingComparatorWithPrecision(0.01)
            .containsSequence(*expected)
    }

    private companion object {
        val DOUBLE_OFFSET: Offset<Double> = Offset.offset(0.05)
    }
}