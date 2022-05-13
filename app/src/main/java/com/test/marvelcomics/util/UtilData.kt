package com.test.marvelcomics.util

import android.annotation.SuppressLint
import androidx.core.util.Pair
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class UtilData {
    companion object {
        private const val formatDate = "yyyy-MM-dd"

        private const val addStrForDateDb = "T00:00:00-0400"

        @SuppressLint("SimpleDateFormat")
        private val patternForStringDate = SimpleDateFormat(formatDate)

        private val patternForDate: DateTimeFormatter = DateTimeFormatter.ofPattern(formatDate)

        private val patternDateDb: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ")

        fun createStringDataRange(firstDay: Date, secondDay: Date): String {
            val firstDayOfMonthStr = patternForStringDate.format(firstDay)
            val nowDateStr = patternForStringDate.format(secondDay)
            return "$firstDayOfMonthStr,$nowDateStr"
        }


        fun createPairDataRange(stringDataRange: String): Pair<Long, Long> {
            val massiveDataRange = createMassiveStringDataRangeForItemOfList(stringDataRange, true)
            val startRange = massiveDataRange[0]
            val endRange = massiveDataRange[1]
            val startRangeLocalDateTime = LocalDate.parse(startRange, patternForDate)
            val endRangeLocalDateTime = LocalDate.parse(endRange, patternForDate)
            val startRangeLong =
                startRangeLocalDateTime.atStartOfDay(ZoneOffset.UTC).toInstant()
                    .toEpochMilli()

            val endRangeLong =
                endRangeLocalDateTime.atStartOfDay(ZoneOffset.UTC).toInstant()
                    .toEpochMilli()

            return Pair(startRangeLong, endRangeLong)
        }

        fun createMassiveStringDataRangeForItemOfList(dataRange: String, isPair: Boolean = false): List<String> {
            val massiveDataRangeFinal = mutableListOf<String>()
            val massiveDataRange = dataRange.split(",", limit = 2)
            if(isPair){
                massiveDataRangeFinal.add(massiveDataRange[0])
                massiveDataRangeFinal.add(massiveDataRange[1])
            }
            else{
                massiveDataRangeFinal.add(massiveDataRange[0] + addStrForDateDb)
                massiveDataRangeFinal.add(massiveDataRange[1] + addStrForDateDb)
            }

            return massiveDataRangeFinal
        }

        fun createZoneDataForItemOfList(saleDay: String?): ZonedDateTime? {
            return ZonedDateTime.parse(saleDay, patternDateDb)
        }
    }

}
