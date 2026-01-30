package com.example.sherpalink

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.sherpalink.auth.SignUpActivity
import com.example.sherpalink.model.NotificationModel
import com.example.sherpalink.screens.SearchDetailsScreen
import com.example.sherpalink.screens.WeatherScreen
import com.example.sherpalink.ui.theme.ui.theme.AboutScreen
import com.example.sherpalink.viewmodel.NotificationViewModel
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import com.example.sherpalink.ui.notifications.NotificationScreen
import com.example.sherpalink.ui.theme.ui.theme.TrendingTripsScreen

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

@RunWith(AndroidJUnit4::class)
class SearchDetailsScreenTest {

    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun openSearchDetailsScreen() {
        // Sample test parameters
        val sampleTitle = "Everest Base Camp Trek"
        val sampleCategory = "Adventure"
        val sampleImageRes = android.R.drawable.ic_menu_gallery // placeholder image

        // Open the screen
        rule.setContent {
            SearchDetailsScreen(
                title = sampleTitle,
                category = sampleCategory,
                imageRes = sampleImageRes,
                onBack = {},      // no-op for test
                onBookClick = {}  // no-op for test
            )
        }

        // Wait for Compose to settle
        rule.waitForIdle()

        // Assertions (optional) to ensure key elements exist
        rule.onNodeWithText(sampleTitle).assertExists()
        rule.onNodeWithText(sampleCategory.uppercase()).assertExists()
        rule.onNodeWithText("Book This Adventure").assertExists()
    }
}

@RunWith(AndroidJUnit4::class)
class AboutScreenTest {

    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun openAboutScreen() {
        // Open the AboutScreen
        rule.setContent {
            AboutScreen()
        }

        // Wait for Compose to settle
        rule.waitForIdle()

        // Assertions to check key elements exist
        rule.onNodeWithText("About SherpaLink").assertExists()
        rule.onNodeWithText("Version 1.0.0").assertExists()
        rule.onNodeWithText(
            "SherpaLink is your trusted travel companion. " +
                    "We help you find the best guides, explore new locations, and make your trips unforgettable. " +
                    "Our goal is to simplify travel planning and connect travelers with local experts."
        ).assertExists()
    }
}

@RunWith(AndroidJUnit4::class)
class NotificationScreenTest {

    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()
@Test
fun openNotificationScreen() {
    rule.setContent {
        val navController = rememberNavController()
        val userId = "test_user"

        // Just open the existing NotificationScreen
        NotificationScreen(navController = navController, userId = userId)
    }

    rule.waitForIdle()

    // Check that the Activity title is visible
    rule.onNodeWithText("Activity").assertExists()
}}

class TrendingTripsScreenTest {

    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun openTrendingTripsScreen() {
        rule.setContent {
            val navController = rememberNavController()
            TrendingTripsScreen(navController = navController)
        }

        rule.waitForIdle()

        // Simple check to make sure the screen opened
        rule.onNodeWithText("All Trending Trips").assertExists()
    }
}
