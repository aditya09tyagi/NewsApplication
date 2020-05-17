package com.example.newsapp.util

import org.threeten.bp.*
import org.threeten.bp.format.DateTimeFormatter
import java.util.*

class DateTimeUtils {

    companion object {
        private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH)
        private val formatter12HourOnlyTime: DateTimeFormatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH)

        fun getLocalDateFromString(date: String): ZonedDateTime {
            val localString = LocalDateTime.parse(date, formatter)
                    .atOffset(ZoneOffset.UTC)
                    .atZoneSameInstant(ZoneId.systemDefault())
                    .format(formatter)
            return ZonedDateTime.parse(localString)
        }

        fun getCurrentLocalDateInUtc(): String {
            return LocalDateTime.parse(ZonedDateTime.now().toInstant().toString(), formatter)
                    .atOffset(ZoneOffset.UTC)
                    .atZoneSameInstant(ZoneId.systemDefault())
                    .format(formatter)
        }

        fun convertLocalDateToUtcDate(localDateTime: LocalDateTime): String {
            return localDateTime
                    .atZone(ZoneId.systemDefault())
                    .withZoneSameInstant(ZoneOffset.UTC)
                    .format(formatter)
        }

        fun getDateInTimeOnlyFormatFromUtcDateTime(startDate: String): String {
            val date = LocalDateTime.parse(startDate, formatter)
            val millis = date.atOffset(ZoneOffset.UTC).atZoneSameInstant(ZoneId.systemDefault()).toInstant().toEpochMilli()
            return Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).format(formatter12HourOnlyTime)
        }

    }
}