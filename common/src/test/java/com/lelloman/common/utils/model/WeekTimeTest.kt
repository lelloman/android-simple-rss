package com.lelloman.common.utils.model

import com.lelloman.common.utils.model.WeekTime.Companion.FRIDAY
import com.lelloman.common.utils.model.WeekTime.Companion.MONDAY
import com.lelloman.common.utils.model.WeekTime.Companion.SATURDAY
import com.lelloman.common.utils.model.WeekTime.Companion.THURSDAY
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Test
import java.util.*

class WeekTimeTest {

    private val random = Random()

    @Test
    fun `throws exception if day of week is 0`() {
        assertThatThrownBy {
            WeekTime(
                dayOfWeek = 0,
                hourOfDay = 0
            )
        }.isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `throws exception if day of week is negative`() {
        assertThatThrownBy {
            WeekTime(
                dayOfWeek = Math.abs(random.nextInt()) * -1,
                hourOfDay = 21
            )
        }.isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `throws exception if day of the week is greater than SATURDAY`() {
        assertThatThrownBy {
            WeekTime(
                dayOfWeek = SATURDAY + 1,
                hourOfDay = 11
            )
        }.isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `throws exception if hour of day is negative`() {
        assertThatThrownBy {
            WeekTime(
                dayOfWeek = MONDAY,
                hourOfDay = Math.abs(random.nextInt()) * -1
            )
        }.isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `throws exception if hour of day is 24`() {
        assertThatThrownBy {
            WeekTime(
                dayOfWeek = THURSDAY,
                hourOfDay = 24
            )
        }.isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `throws exception if hour of day is greater than 24`() {
        assertThatThrownBy {
            WeekTime(
                dayOfWeek = FRIDAY,
                hourOfDay = 24 + Math.abs(random.nextInt())
            )
        }.isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `does not throw exception for valid arguments`() {
        for (day in 1..7) {
            for (hour in 0..23) {

                val tested = WeekTime(hourOfDay = hour, dayOfWeek = day)

                assertThat(tested.hourOfDay).isEqualTo(hour)
                assertThat(tested.dayOfWeek).isEqualTo(day)
            }
        }
    }
}