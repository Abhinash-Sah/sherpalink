package com.example.sherpalink

import com.example.sherpalink.repository.UserRepo
import com.example.sherpalink.viewmodel.UserViewModel
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class UserViewModelTest {

    // Rule to handle Coroutines in tests
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun login_success_test() = runTest {
        val repo = mock<UserRepo>()
        val viewModel = UserViewModel(repo)

        // Mock the repository behavior to trigger the callback
        doAnswer { invocation ->
            val callback = invocation.getArgument<(Boolean, String) -> Unit>(2)
            callback(true, "Login success")
            null
        }.`when`(repo).login(eq("test@gmail.com"), eq("123456"), any())

        var successResult = false
        var messageResult = ""

        // Calling the login method
        viewModel.login("test@gmail.com", "123456") { success, msg ->
            successResult = success
            messageResult = msg
        }

        // Assertions will now wait for the callback to complete
        assertTrue("Expected login to be successful", successResult)
        assertEquals("Login success", messageResult)

        verify(repo).login(eq("test@gmail.com"), eq("123456"), any())
    }
}