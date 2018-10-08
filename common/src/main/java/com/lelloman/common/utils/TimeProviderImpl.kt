package com.lelloman.common.utils

import com.lelloman.common.utils.model.DayTime
import com.lelloman.common.utils.model.Time
import com.lelloman.common.utils.model.WeekTime
import java.util.*

class TimeProviderImpl : TimeProvider {

    private val calendar: Calendar = Calendar.getInstance()

    override fun getTime(utcMs: Long): Time {
        calendar.timeInMillis = utcMs
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        return Time(
            weekTime = WeekTime(
                dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK),
                hourOfDay = hour
            ),
            dayTime = DayTime(
                hour = hour,
                minute = calendar.get(Calendar.MINUTE)
            )
        )
    }

    override fun nowUtcMs() = System.currentTimeMillis()

    override fun now() = getTime(nowUtcMs())
}