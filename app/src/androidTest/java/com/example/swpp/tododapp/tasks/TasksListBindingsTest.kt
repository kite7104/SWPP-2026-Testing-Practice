package com.example.swpp.tododapp.tasks

import android.view.View
import android.widget.TextView
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import androidx.test.platform.app.InstrumentationRegistry
import com.example.swpp.tododapp.data.Task
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate

@SmallTest
@RunWith(AndroidJUnit4::class)
class TasksListBindingsTest {

    private lateinit var textView: TextView

    @Before
    fun setUp() {
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            textView = TextView(InstrumentationRegistry.getInstrumentation().targetContext)
        }
    }

    private fun bind(task: Task?) {
        InstrumentationRegistry.getInstrumentation().runOnMainSync { setDueDateLabel(textView, task) }
    }

    private fun assertShows(expected: String) {
        assertEquals(expected, textView.text.toString())
        assertEquals(View.VISIBLE, textView.visibility)
    }

    /** "No label" = the user sees nothing: view not visible, or visible with empty text. */
    private fun assertNoLabel() {
        assertTrue(
            "expected no label but was '${textView.text}' (visibility=${textView.visibility})",
            textView.visibility != View.VISIBLE || textView.text.isEmpty()
        )
    }

    private val today get() = LocalDate.now()

    // ---------- Success cases ----------
    @Test
    fun setDueDateLabel_activeDueIn3Days_showsDMinus3() {
        // GIVEN / WHEN
        bind(Task("T", "D", false, "id1", today.plusDays(3)))
        // THEN
        assertShows("D-3")
    }

    @Test
    fun setDueDateLabel_activeOverdue2Days_showsDPlus2() {
        bind(Task("T", "D", false, "id1", today.minusDays(2)))
        assertShows("D+2")
    }

    @Test
    fun setDueDateLabel_completedWithDueDate_showsClear() {
        bind(Task("T", "D", true, "id1", today.plusDays(3)))
        assertShows("CLEAR")
    }

    @Test
    fun setDueDateLabel_completedWithoutDueDate_showsClear() {
        bind(Task("T", "D", true, "id1", null))
        assertShows("CLEAR")
    }

    @Test
    fun setDueDateLabel_activeWithoutDueDate_showsNoLabel() {
        bind(Task("T", "D", false, "id1", null))
        assertNoLabel()
    }

    // ---------- Edge cases ----------
    @Test
    fun setDueDateLabel_nullTask_showsNoLabel() {
        bind(null)
        assertNoLabel()
    }

    @Test
    fun setDueDateLabel_activeDueToday_showsDDay() {
        bind(Task("T", "D", false, "id1", today))
        assertShows("D-Day")
    }

    @Test
    fun setDueDateLabel_rebindCompletedNoDateToActive_clearsClear() {
        // GIVEN - row currently shows CLEAR
        bind(Task("T", "D", true, "id1", null))
        assertShows("CLEAR")
        // WHEN - the task is unchecked
        bind(Task("T", "D", false, "id1", null))
        // THEN - CLEAR is gone
        assertNoLabel()
    }

    @Test
    fun setDueDateLabel_rebindDMinus3ToNoDueDate_clearsLabel() {
        bind(Task("T", "D", false, "id1", today.plusDays(3)))
        assertShows("D-3")
        bind(Task("T", "D", false, "id2", null))
        assertNoLabel()
    }

    @Test
    fun setDueDateLabel_rebindActiveNoDateToCompleted_showsClear() {
        bind(Task("T", "D", false, "id1", null))
        bind(Task("T", "D", true, "id1", null))
        assertShows("CLEAR")
    }
}
