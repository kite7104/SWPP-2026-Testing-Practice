package com.example.swpp.tododapp.tasks

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.hasSibling
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.swpp.tododapp.R
import com.example.swpp.tododapp.ServiceLocator
import com.example.swpp.tododapp.data.Task
import com.example.swpp.tododapp.data.source.FakeAndroidTestRepository
import kotlinx.coroutines.test.runTest
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.anyOf
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@MediumTest
@RunWith(AndroidJUnit4::class)
class TasksFragmentDueDateTest {

    private lateinit var repository: FakeAndroidTestRepository

    @Before
    fun initRepository() {
        repository = FakeAndroidTestRepository()
        ServiceLocator.tasksRepository = repository
    }

    @After
    fun cleanupDb() = runTest {
        ServiceLocator.resetRepository()
    }

    private val checkbox = allOf(withId(R.id.complete_checkbox), hasSibling(withText("TITLE1")))
    private val label = allOf(withId(R.id.due_date_text), hasSibling(withText("TITLE1")))

    @Test
    fun tasksList_checkThenUncheckTaskWithoutDueDate_clearLabelDisappears() = runTest {
        // GIVEN - task list with one active task without a due date
        repository.saveTask(Task("TITLE1", "DESC1", false, "id1", null))
        launchFragmentInContainer<TasksFragment>(Bundle(), R.style.AppTheme)

        // WHEN - check it
        onView(checkbox).perform(click())
        // THEN - CLEAR is shown
        onView(label).check(matches(allOf(isDisplayed(), withText("CLEAR"))))

        // WHEN - uncheck it
        onView(checkbox).perform(click())
        // THEN - CLEAR is gone (hidden or empty)
        onView(label).check(matches(anyOf(not(isDisplayed()), withText(""))))
    }
}
