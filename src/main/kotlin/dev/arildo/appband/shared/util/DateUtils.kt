package dev.arildo.appband.shared.util

import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale


fun Long.toHour(): String {
    val datePattern = DateTimeFormatter.ofPattern("HH:mm")
            .withZone(ZoneId.systemDefault())
    return datePattern.format(Instant.ofEpochMilli(this))
}

fun Date.toDayTime(): Long {
    val calendar = Calendar.getInstance()
    calendar.time = this
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH) + 1
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val localDate = LocalDate.of(year, month, day)
    return Timestamp.valueOf(localDate.atTime(LocalTime.NOON)).time
}

fun Date.minutesFromDate(minutes: Int): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.add(Calendar.MINUTE, minutes)
    return calendar.time
}