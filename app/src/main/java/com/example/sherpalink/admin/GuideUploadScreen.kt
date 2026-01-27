package com.example.sherpalink.admin

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.example.sherpalink.model.GuideModel
import com.example.sherpalink.viewmodel.GuideViewModel

@Composable
fun AdminGuideUploadScreen(navController: NavHostController, guideViewModel: GuideViewModel) {
    val context = LocalContext.current

    // 1. Observe State at the TOP level
    val status by guideViewModel.status.observeAsState()
    var isUploading by remember { mutableStateOf(false) }

    // 2. Handle Navigation Side-Effect
    LaunchedEffect(status) {
        if (status == "Guide added successfully!") {
            isUploading = false
            Toast.makeText(context, status, Toast.LENGTH_SHORT).show()
            navController.popBackStack()
        } else if (status != null && status!!.contains("Error")) {
            isUploading = false
            Toast.makeText(context, status, Toast.LENGTH_LONG).show()
        }
    }

    var name by remember { mutableStateOf("") }
    var specialty by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var experience by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri -> selectedImageUri = uri }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Register New Guide", style = MaterialTheme.typography.headlineMedium)

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clickable {
                    photoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                if (selectedImageUri != null) {
                    AsyncImage(model = selectedImageUri, contentDescription = null, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                } else {
                    Text("Tap to select Guide Photo", color = MaterialTheme.colorScheme.error)
                }
            }
        }

        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Full Name") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = specialty, onValueChange = { specialty = it }, label = { Text("Specialty") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = experience, onValueChange = { experience = it }, label = { Text("Years of Experience") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = location, onValueChange = { location = it }, label = { Text("Location") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = price, onValueChange = { price = it }, label = { Text("Price Per Day ($)") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Phone Number") }, modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            modifier = Modifier.fillMaxWidth().height(56.dp),
            enabled = !isUploading,
            onClick = {
                if (name.isBlank() || price.isBlank() || selectedImageUri == null) {
                    Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                isUploading = true
                val guide = GuideModel(
                    name = name,
                    specialty = specialty,
                    experienceYears = experience.toIntOrNull() ?: 0,
                    location = location,
                    pricePerDay = price.toDoubleOrNull() ?: 0.0,
                    phone = phone
                )
                guideViewModel.addGuide(guide, selectedImageUri)
            }
        ) {
            if (isUploading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text("Save Guide")
            }
        }
    }
}