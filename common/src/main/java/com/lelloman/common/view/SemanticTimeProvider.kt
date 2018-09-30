package com.lelloman.common.view

import com.lelloman.common.R
import com.lelloman.common.utils.TimeProvider
import java.text.SimpleDateFormat
import java.util.*

class SemanticTimeProvider(
    private val timeProvider: TimeProvider,
    private val resourceProvider: ResourceProvider,
    givenTimeZone: TimeZone = TimeZone.getDefault()
) {

    private val dateTimeFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US).apply {
        this.timeZone = givenTimeZone
    }

    private val minutesThreshold = (60 * 60) - 1
    private val hoursThreshold = (60 * 60 * 24) - 1

    fun getDateTimeString(time: Long = timeProvider.nowUtcMs()): String = dateTimeFormat.format(time)

    fun getTimeQuantity(ms: Long): String {
        val totSeconds = ms / 1000
        val quantityStrings = resourceProvider.getStringArray(R.array.time_quantities)

        if (ms <= 0L) {
            return resourceProvider.getString(R.string.time_quantity_sec, 0)
        }

        return arrayOf(
            totSeconds % 60,
            (totSeconds / 60) % 60,
            (totSeconds / 3600) % 24,
            (totSeconds / (3600 * 24))
        )
            .mapIndexed { index, quantity ->
                quantity to quantityStrings[index].format(quantity)
            }
            .filter { (quantity, _) ->
                quantity > 0
            }
            .joinToString(" ") { (_, string) -> string }
    }

    fun getTimeDiffString(timeUtcMs: Long): String {
        var delta = (timeProvider.nowUtcMs() - timeUtcMs) / 1000
        if (delta < 0) {
            delta = 0
        }

        val stringId: Int
        val value: Long

        when {
            delta <= 59 -> {
                stringId = R.string.time_diff_seconds
                value = delta
            }
            delta <= minutesThreshold -> {
                stringId = R.string.time_diff_minutes
                value = delta / 60
            }
            delta <= hoursThreshold -> {
                stringId = R.string.time_diff_hours
                value = delta / 3600
            }
            else -> {
                stringId = R.string.time_diff_days
                value = delta / (3600 * 24)
            }
        }

        return resourceProvider.getString(stringId, value)
    }
}