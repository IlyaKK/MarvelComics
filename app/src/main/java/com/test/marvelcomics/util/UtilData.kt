package com.test.marvelcomics.util

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.util.Pair
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class UtilData {
    companion object {
        private const val addStrForDateDb = "T00:00:00-0400"

        @SuppressLint("SimpleDateFormat")
        private val patternDateFormatForString = SimpleDateFormat("yyyy-MM-dd")

        @RequiresApi(Build.VERSION_CODES.O)
        private val patternDateFormatForDate: DateTimeFormatter =
            DateTimeFormatter.ofPattern(patternDateFormatForString.toPattern())

        @RequiresApi(Build.VERSION_CODES.O)
        private val patternDateForDataBase: DateTimeFormatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ")

        fun createStringDataRange(firstDay: Date, secondDay: Date): String {
            val firstDayOfMonthStr = patternDateFormatForString.format(firstDay)
            val nowDateStr = patternDateFormatForString.format(secondDay)
            return "$firstDayOfMonthStr,$nowDateStr"
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun createPairDataRange(stringDataRange: String): Pair<Long, Long> {
            val massiveDataRange = createMassiveDatesFromStringDataRange(stringDataRange)
            val startRangeLocalDateTime =
                LocalDate.parse(massiveDataRange[0], patternDateFormatForDate)
            val endRangeLocalDateTime =
                LocalDate.parse(massiveDataRange[1], patternDateFormatForDate)
            val startRangeLong =
                startRangeLocalDateTime.atStartOfDay(ZoneOffset.UTC).toInstant()
                    .toEpochMilli()

            val endRangeLong =
                endRangeLocalDateTime.atStartOfDay(ZoneOffset.UTC).toInstant()
                    .toEpochMilli()

            return Pair(startRangeLong, endRangeLong)
        }

        fun createMassiveDatesFromStringDataRange(dataRange: String): List<String> {
            val massiveDataRangeFinal = mutableListOf<String>()
            val massiveDataRange = dataRange.split(",", limit = 2)
            massiveDataRangeFinal.add(massiveDataRange[0] + addStrForDateDb)
            massiveDataRangeFinal.add(massiveDataRange[1] + addStrForDateDb)
            return massiveDataRangeFinal
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun createZoneDataForItemOfList(saleDay: String?): ZonedDateTime? {
            return ZonedDateTime.parse(saleDay, patternDateForDataBase)
        }
    }

}
