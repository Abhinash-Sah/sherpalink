package com.example.sherpalink

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.sherpalink.auth.SignUpActivity
import com.example.sherpalink.model.GuideModel
import com.example.sherpalink.screens.HomeScreen
import com.example.sherpalink.screens.WeatherScreen
import com.example.sherpalink.ui.guide.GuideBookingScreen
import com.example.sherpalink.viewmodel.GuideViewModel
import com.example.sherpalink.viewmodel.GuideViewModelFactory
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FullAppInstrumentedTest {

    // ---------------- SIGN UP ACTIVITY ----------------
    @get:Rule
    val signUpRule = createAndroidComposeRule<SignUpActivity>()

    @Test
    fun signUpFlow() {
        signUpRule.waitForIdle()

        signUpRule.onNode(hasText("First Name")).performTextInput("Ram")
        signUpRule.onNode(hasText("Last Name")).performTextInput("Hero")
        signUpRule.onNode(hasText("Email")).performTextInput("rammmmm6464@gmail.com")
        signUpRule.onNode(hasText("Phone")).performTextInput("9812345678")
        signUpRule.onNode(hasText("Password")).performTextInput("password123")
        signUpRule.onNode(hasText("Confirm Password")).performTextInput("password123")

        signUpRule.onNode(hasText("Guide")).performClick()
        signUpRule.onNode(hasText("Sign Up")).performClick()

        signUpRule.waitForIdle()
    }

    // ---------------- SIGN IN FLOW ----------------
    @Test
    fun signInFlow() {
        signUpRule.waitForIdle()

        signUpRule.onNode(hasText("Email")).performTextInput("rammmmm6464@gmail.com")
        signUpRule.onNode(hasText("Password")).performTextInput("password123")
        signUpRule.onNode(hasText("Sign In")).performClick()

        signUpRule.waitForIdle()
    }

    // ---------------- WEATHER SCREEN ----------------
    @Test
    fun openWeatherScreen() {
        signUpRule.activityRule.scenario.onActivity { activity ->
            activity.setContent {
                val navController = rememberNavController()
                WeatherScreen(navController)
            }
        }
        signUpRule.waitForIdle()
    }

    // ---------------- DASHBOARD ACTIVITY ----------------
    @RunWith(AndroidJUnit4::class)
    class DashboardTest {

        @get:Rule
        val dashboardRule = createAndroidComposeRule<DashboardActivity>()

        @Test
        fun openDashboard_and_navigateTabs() {
            dashboardRule.waitForIdle()

            dashboardRule.onNode(hasText("Home")).assertExists()
            dashboardRule.onNode(hasText("Profile")).performClick()

            dashboardRule.waitForIdle()
        }
    }}

// ---------------- GUIDE BOOKING SCREEN ----------------
@RunWith(AndroidJUnit4::class)
class GuideBookingScreenTest {

    @get:Rule
    val bookingRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun openGuideBooking_and_clickBook() {

        bookingRule.activity.setContent {
            val navController = rememberNavController()

        }

        bookingRule.waitForIdle()

        // Check screen loaded
        bookingRule.onAllNodes(hasText("Book"))
            .onFirst()
            .assertExists()

        // Enter people count if field exists
        bookingRule.onAllNodes(hasText("Number of People"))
            .onFirst()
            .performTextInput("2")

        // Click book
        bookingRule.onAllNodes(hasText("Book"))
            .onFirst()
            .performClick()

        bookingRule.waitForIdle()
    }
}
