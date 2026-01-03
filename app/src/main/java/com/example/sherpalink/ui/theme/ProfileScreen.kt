package com.example.sherpalink.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.sherpalink.R
import com.example.sherpalink.UserModel
import com.example.sherpalink.repository.UserRepoImplementation
import com.example.sherpalink.viewmodel.UserViewModel

@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: UserViewModel = viewModel(
        factory = UserViewModel.UserViewModelFactory(UserRepoImplementation())
    )
) {
    val user = viewModel.user
    val loading = viewModel.loading
    var showEditDialog by remember { mutableStateOf(false) }
    var showDeleteConfirm by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(viewModel.user) {
        val uid = viewModel.getCurrentUser()?.uid
        if (uid != null && viewModel.user == null) {
            viewModel.getUserById(uid)
        }
    }

    if (loading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    if (user == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("User data not found")
        }
        return
    }

    ProfileScreenContent(
        user = user,
        onEditClick = { showEditDialog = true },
        onDeleteClick = { showDeleteConfirm = true },
        showEditDialog = showEditDialog,
        onDismissEdit = { showEditDialog = false },
        onSaveEdit = { updatedUser ->
            viewModel.updateProfile(updatedUser.userId, updatedUser) { _, _ -> }
        }
    )

    // ------------------ Delete Confirmation Dialog ------------------
    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("Delete Account") },
            text = { Text("Are you sure you want to delete your account? This action cannot be undone.") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteAccount(user.userId) { success, message ->
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        if (success) {
                            navController.navigate("login_screen") {
                                popUpTo("home") { inclusive = true }
                            }
                        }
                    }
                    showDeleteConfirm = false
                }) {
                    Text("Delete", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun ProfileScreenContent(
    user: UserModel,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    showEditDialog: Boolean,
    onDismissEdit: () -> Unit,
    onSaveEdit: (UserModel) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(user.role.uppercase(), fontSize = 18.sp, fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.height(24.dp))

        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "Profile Image",
            modifier = Modifier
                .size(150.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text("${user.firstName} ${user.lastName}", fontSize = 26.sp, fontWeight = FontWeight.ExtraBold)
        Spacer(modifier = Modifier.height(12.dp))
        Text(user.email)
        Spacer(modifier = Modifier.height(28.dp))

        Button(
            onClick = onEditClick,
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth(0.85f).height(52.dp)
        ) { Text("Details", color = Color.White) }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = onDeleteClick,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD9362B)),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth(0.85f).height(52.dp)
        ) { Text("Delete Account", color = Color.White) }
    }

    if (showEditDialog) {
        EditProfileDialog(
            user = user,
            onDismiss = onDismissEdit,
            onSave = onSaveEdit
        )
    }
}

// ------------------------------ Edit Dialog ------------------------------
@Composable
fun EditProfileDialog(
    user: UserModel,
    onDismiss: () -> Unit,
    onSave: (UserModel) -> Unit
) {
    var firstName by remember { mutableStateOf(user.firstName) }
    var lastName by remember { mutableStateOf(user.lastName) }
    var email by remember { mutableStateOf(user.email) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Profile") },
        text = {
            Column {
                OutlinedTextField(
                    value = firstName,
                    onValueChange = { firstName = it },
                    label = { Text("First Name") }
                )
                OutlinedTextField(
                    value = lastName,
                    onValueChange = { lastName = it },
                    label = { Text("Last Name") }
                )
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") }
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onSave(user.copy(firstName = firstName, lastName = lastName, email = email))
                onDismiss()
            }) { Text("Save") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    val mockUser = UserModel(
        userId = "1",
        firstName = "Saksham",
        lastName = "Ban",
        email = "saksham@example.com",
        dob = "2000-01-01",
        gender = "Male",
        role = "user"
    )

    ProfileScreenContent(
        user = mockUser,
        onEditClick = {},
        onDeleteClick = {},
        showEditDialog = false,
        onDismissEdit = {},
        onSaveEdit = {}
    )
}
