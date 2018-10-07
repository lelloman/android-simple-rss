package com.lelloman.common.utils

import com.lelloman.common.utils.model.DayTime
import com.lelloman.common.utils.model.WeekTime
import java.util.*

class TimeProviderImpl : TimeProvider {

    private val calendar: Calendar = Calendar.getInstance()

    override fun getParsedWeekTime(timeMs: Long): WeekTime = synchronized(calendar) {
        calendar.timeInMillis = timeMs
        WeekTime(
            dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK),
            hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
        )
    }

    override fun getParsedDayTime(timeMs: Long): DayTime = synchronized(calendar) {
        calendar.timeInMillis = timeMs
        DayTime(
            hour = calendar.get(Calendar.HOUR_OF_DAY),
            minute = calendar.get(Calendar.MINUTE)
        )
    }

    override fun nowUtcMs() = System.currentTimeMillis()
}