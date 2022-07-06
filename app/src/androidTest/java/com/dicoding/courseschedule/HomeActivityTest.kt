package com.dicoding.courseschedule

import android.app.Activity
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import androidx.test.runner.lifecycle.Stage
import com.dicoding.courseschedule.ui.add.AddCourseActivity
import com.dicoding.courseschedule.ui.home.HomeActivity
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class HomeActivityTest {

    @get:Rule
    var activityRule = ActivityScenarioRule(HomeActivity::class.java)

    @Test
    fun test1AddCourse() {
        Espresso.onView(ViewMatchers.withId(R.id.action_add))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed())).perform(ViewActions.click())

        val currentActivity = getCurrentActivity()
        Assert.assertTrue(currentActivity?.javaClass == AddCourseActivity::class.java)
    }

    private fun getCurrentActivity(): Activity? {
        var currentActivity: Activity? = null
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            run {
                currentActivity = ActivityLifecycleMonitorRegistry.getInstance()
                    .getActivitiesInStage(Stage.RESUMED).elementAtOrNull(0)
            }
        }
        return currentActivity
    }

}