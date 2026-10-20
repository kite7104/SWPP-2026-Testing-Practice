package com.example.swpp.tododapp.tasks

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class DueDateUtilsTest {

    private fun label(due: String?, today: String, completed: Boolean = false): String =
        dueDateLabel(due?.let { LocalDate.parse(it) }, LocalDate.parse(today), completed)

    // ---------- Success cases ----------
    @Test
    fun dueDateLabel_completedWithOverdueDate_returnsClear() {
        // GIVEN a completed, overdue task / WHEN / THEN
        assertEquals("CLEAR", label("2026-10-15", "2026-10-20", completed = true))
    }

    @Test
    fun dueDateLabel_completedWithoutDueDate_returnsClear() {
        assertEquals("CLEAR", label(null, "2026-10-20", completed = true))
    }

    @Test
    fun dueDateLabel_activeWithoutDueDate_returnsEmpty() {
        assertEquals("", label(null, "2026-10-20"))
    }

    @Test
    fun dueDateLabel_dueIn3DaysSameMonth_returnsDMinus3() {
        assertEquals("D-3", label("2026-10-23", "2026-10-20"))
    }

    @Test
    fun dueDateLabel_dueIn36DaysNextMonth_returnsDMinus36() {
        assertEquals("D-36", label("2026-11-25", "2026-10-20"))
    }

    @Test
    fun dueDateLabel_overdue3DaysSameMonth_returnsDPlus3() {
        assertEquals("D+3", label("2026-10-17", "2026-10-20"))
    }

    @Test
    fun dueDateLabel_overdue40DaysPrevMonth_returnsDPlus40() {
        assertEquals("D+40", label("2026-09-10", "2026-10-20"))
    }

    // ---------- Edge cases: day 0 ----------
    @Test
    fun dueDateLabel_dueToday_returnsDDay() {
        assertEquals("D-Day", label("2026-10-20", "2026-10-20"))
    }

    @Test
    fun dueDateLabel_dueTomorrow_returnsDMinus1() {
        assertEquals("D-1", label("2026-10-21", "2026-10-20"))
    }

    @Test
    fun dueDateLabel_dueYesterday_returnsDPlus1() {
        assertEquals("D+1", label("2026-10-19", "2026-10-20"))
    }

    // ---------- Edge cases: exactly one month ----------
    @Test
    fun dueDateLabel_dueExactlyOneMonthLater_returnsDMinus31() {
        assertEquals("D-31", label("2026-11-20", "2026-10-20"))
    }

    @Test
    fun dueDateLabel_dueOneMonthMinusOneDay_returnsDMinus30() {
        assertEquals("D-30", label("2026-11-19", "2026-10-20"))
    }

    @Test
    fun dueDateLabel_dueOneMonthPlusOneDay_returnsDMinus32() {
        assertEquals("D-32", label("2026-11-21", "2026-10-20"))
    }

    @Test
    fun dueDateLabel_overdueExactlyOneMonth_returnsDPlus30() {
        assertEquals("D+30", label("2026-09-20", "2026-10-20"))
    }

    // ---------- Edge cases: month / year end, leap day ----------
    @Test
    fun dueDateLabel_dueFirstOfNextMonth_returnsDMinus1() {
        assertEquals("D-1", label("2026-11-01", "2026-10-31"))
    }

    @Test
    fun dueDateLabel_dueNewYearsDay_returnsDMinus1() {
        assertEquals("D-1", label("2027-01-01", "2026-12-31"))
    }

    @Test
    fun dueDateLabel_dueExactlyOneYearLater_returnsDMinus365() {
        assertEquals("D-365", label("2027-10-20", "2026-10-20"))
    }

    @Test
    fun dueDateLabel_acrossLeapDay_returnsDMinus2() {
        assertEquals("D-2", label("2028-03-01", "2028-02-28"))
    }

    @Test
    fun dueDateLabel_jan31ToMar1LeapYear_returnsDMinus30() {
        assertEquals("D-30", label("2028-03-01", "2028-01-31"))
    }

    @Test
    fun dueDateLabel_completedDueToday_returnsClear() {
        assertEquals("CLEAR", label("2026-10-20", "2026-10-20", completed = true))
    }
}
