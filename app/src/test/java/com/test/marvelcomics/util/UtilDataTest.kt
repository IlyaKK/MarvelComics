package com.test.marvelcomics.util

import androidx.core.util.Pair
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate
import java.time.ZoneOffset
import java.util.*


class UtilDataTest {
    @Test
    fun `creating string from two Data dates`() {
        val firstDate = Date(2022 - 1900, 0, 1)
        val secondDate = Date(2022 - 1900, 1, 2)
        val actualStringDate = UtilData.createStringDataRange(firstDate, secondDate)
        val expectedStringDate = "2022-01-01,2022-02-02"
        assertEquals(expectedStringDate, actualStringDate)
    }

    @Test
    fun `creating Pair from string dates`() {
        val stringDates = "2022-01-01,2022-02-01"
        val actualPair = UtilData.createPairDataRange(stringDates)
        val firstData = LocalDate.of(2022, 1, 1)
        val secondData = LocalDate.of(2022, 2, 1)
        val expectedPair = Pair(
            firstData.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli(),
            secondData.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
        )
        assertEquals(expectedPair, actualPair)
    }
}