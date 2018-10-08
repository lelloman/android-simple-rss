package com.lelloman.common.utils

import com.lelloman.common.utils.model.WeekTime
import com.lelloman.common.utils.model.WeekTime.Companion.SATURDAY
import com.lelloman.common.utils.model.WeekTime.Companion.SUNDAY
import com.lelloman.common.utils.model.WeekTime.Companion.TUESDAY
import com.lelloman.common.utils.model.WeekTime.Companion.WEDNESDAY
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.text.SimpleDateFormat

class TimeProviderImplTest {

    val tested = TimeProviderImpl()

    @Test
    fun `returns sunday at 9 am`() {
        val timeMs = SDF.parse("2018/10/07 09:22:22").time

        val time = tested.getTime(timeMs)

        assertThat(time.weekTime).isEqualTo(WeekTime(
            dayOfWeek = SUNDAY,
            hourOfDay = 9
        ))
    }

    @Test
    fun `returns saturday at 9 pm`() {
        val timeMs = SDF.parse("2018/10/06 21:22:22").time

        val time = tested.getTime(timeMs)

        assertThat(time.weekTime).isEqualTo(WeekTime(
            dayOfWeek = SATURDAY,
            hourOfDay = 21
        ))
    }

    @Test
    fun `returns wednesday at midnight`() {
        val timeMs = SDF.parse("2000/01/05 00:00:00").time

        val time = tested.getTime(timeMs)

        assertThat(time.weekTime).isEqualTo(WeekTime(
            dayOfWeek = WEDNESDAY,
            hourOfDay = 0
        ))
    }

    @Test
    fun `returns tuesday at noon`() {
        val timeMs = SDF.parse("2004/02/03 12:59:59").time

        val time = tested.getTime(timeMs)

        assertThat(time.weekTime).isEqualTo(WeekTime(
            dayOfWeek = TUESDAY,
            hourOfDay = 12
        ))
    }

    private companion object {
        val SDF = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
    }
}