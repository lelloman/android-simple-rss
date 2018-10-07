package com.lelloman.common.utils.model

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Test
import java.util.*
import kotlin.math.abs

class DayTimeTest {

    private val random = Random()

    @Test
    fun `throws exception if hour is minus 1`() {
        assertThatThrownBy {
            DayTime(hour = -1, minute = 0)
        }.isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `throws exception if hour is less than 0`() {
        assertThatThrownBy {
            DayTime(hour = -1 * abs(random.nextInt()), minute = 10)
        }.isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `throws exception if hour is 24`() {
        assertThatThrownBy {
            DayTime(hour = 24, minute = 40)
        }.isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `throws exception if hour is greater than 24`() {
        assertThatThrownBy {
            DayTime(hour = 24 + abs(random.nextInt()), minute = 59)
        }.isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `throws exception if minute is minus 1`() {
        assertThatThrownBy {
            DayTime(hour = 0, minute = -1)
        }.isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `throws exception if minute is less than 0`() {
        assertThatThrownBy {
            DayTime(hour = 3, minute = -1 * abs(random.nextInt()))
        }.isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `throws exception if minute is 60`() {
        assertThatThrownBy {
            DayTime(hour = 23, minute = 60)
        }.isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `throws exception if minuter is greater than 60`() {
        assertThatThrownBy {
            DayTime(hour = 13, minute = 60 + abs(random.nextInt()))
        }.isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `does not throw exception for valid arguments`() {
        for (hour in 0 until 24) {
            for (minute in 0 until 60) {

                val tested = DayTime(hour = hour, minute = minute)

                assertThat(tested.hour).isEqualTo(hour)
                assertThat(tested.minute).isEqualTo(minute)
            }
        }
    }
}