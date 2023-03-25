package com.mundcode.muntam

import com.mundcode.muntam.util.asCurrentTimerText
import org.junit.Assert.assertEquals
import org.junit.Test

class TimerTextTest {

    @Test
    fun testPositiveTime() {
        assertEquals("00:00:59", 59L.asCurrentTimerText())
        assertEquals("00:01:00", 60L.asCurrentTimerText())
        assertEquals("01:00:00", 3600L.asCurrentTimerText())
        assertEquals("01:23:45", 5025L.asCurrentTimerText())
        assertEquals("23:59:59", 86399L.asCurrentTimerText())
    }

    @Test
    fun testNegativeTime() {
        assertEquals("-00:00:59", (-59L).asCurrentTimerText())
        assertEquals("-00:01:00", (-60L).asCurrentTimerText())
        assertEquals("-01:00:00", (-3600L).asCurrentTimerText())
        assertEquals("-01:23:45", (-5025L).asCurrentTimerText())
        assertEquals("-23:59:59", (-86399L).asCurrentTimerText())
    }

    @Test
    fun testZeroTime() {
        assertEquals("00:00:00", 0L.asCurrentTimerText())
    }
}