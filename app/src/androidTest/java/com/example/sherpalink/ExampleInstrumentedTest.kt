package com.example.sherpalink

import androidx.activity.compose.setContent
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.sherpalink.auth.SignUpActivity
import com.example.sherpalink.screens.WeatherScreen
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AuthFlowInstrumentedTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<SignUpActivity>()

    @Test
    fun SignUp() {
        composeRule.waitForIdle()

        // ---------- SIGN UP ----------
        composeRule.onNode(hasText("First Name")).performTextInput("Ram")     // First Name
        composeRule.onNode(hasText("Last Name")).performTextInput("Hero")     // Last Name
        composeRule.onNode(hasText("Email")).performTextInput("rammmmm6464@gmail.com")// Email
        composeRule.onNode(hasText("Phone")).performTextInput("9812345678")   // Phone
        composeRule.onNode(hasText("Password")).performTextInput("password123")  // Password
        composeRule.onNode(hasText("Confirm Password"))
            .performTextInput("password123") // Confirm Password

        // Choose Guide role
        composeRule.onNode(hasText("Guide")).performClick()

        // Click Sign Up
        composeRule.onNode(hasText("Sign Up")).performClick()

        composeRule.waitForIdle()
    }

    @Test
    fun SignInActivity() {
        // ---------- SIGN IN ----------
        composeRule.onNode(hasText("Email")).performTextInput("rammmmm6464@gmail.com")
        composeRule.onNode(hasText("Password")).performTextInput("password123")
        composeRule.onNode(hasText("Sign In")).performClick()

        composeRule.waitForIdle()
    }

    @Test
    fun signIn_then_openWeatherScreen() {
        composeRule.waitForIdle()

        // ---------- SIGN IN ----------
        composeRule.onNode(hasText("Email"))
            .performTextInput("rammmmm6464@gmail.com")
        composeRule.onNode(hasText("Password"))
            .performTextInput("password123")
        composeRule.onNode(hasText("Sign In"))
            .performClick()

        composeRule.waitForIdle()

        // ---------- OPEN WEATHER SCREEN ----------
        composeRule.activityRule.scenario.onActivity { activity ->
            activity.setContent {
                val navController = rememberNavController()
                WeatherScreen(navController = navController)
            }
        }

        composeRule.waitForIdle()
    }

    // ----------  DASHBOARD TEST ----------
    class DashboardInstrumentedTest {

        @get:Rule
        val composeRule = createAndroidComposeRule<DashboardActivity>()

        @Test
        fun openDashboard() {
            composeRule.waitForIdle()

            // Verify Home tab exists
            composeRule.onNode(hasText("Home")).assertExists()

            composeRule.onNode(hasText("Profile")).performClick()
            composeRule.waitForIdle()
        }
    }}
