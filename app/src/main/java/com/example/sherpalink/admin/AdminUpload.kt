package com.example.sherpalink.screens.admin

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.sherpalink.ProductModel
import com.example.sherpalink.R
import com.example.sherpalink.viewmodel.ProductViewModel

@Composable
fun AdminTourUploadScreen(
    navController: androidx.navigation.NavHostController,
    productViewModel: ProductViewModel
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    // State variables
    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var isUploading by remember { mutableStateOf(false) }

    // ✅ Use the modern Photo Picker launcher
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        selectedImageUri = uri
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Add New Tour Package",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // ✅ Image Selection Area
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .clickable {
                    photoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                },
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                if (selectedImageUri != null) {
                    AsyncImage(
                        model = selectedImageUri,
                        contentDescription = "Selected Image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            painter = painterResource(id = R.drawable.outline_add_a_photo_24),
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Tap to add a photo", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Input Fields
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Tour/Product Name") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = price,
            onValueChange = { price = it },
            label = { Text("Price ($)") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = category,
            onValueChange = { category = it },
            label = { Text("Category (e.g., Trek, Tour)") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 4
        )

        Spacer(modifier = Modifier.height(16.dp))

        // ✅ Unified Upload Logic
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            enabled = !isUploading,
            onClick = {
                if (selectedImageUri == null || name.isBlank() || price.isBlank()) {
                    Toast.makeText(context, "Please complete all fields", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                val priceValue = price.toDoubleOrNull()
                if (priceValue == null) {
                    Toast.makeText(context, "Invalid price format", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                isUploading = true

                // Step 1: Upload Image to Cloudinary via Repository
                productViewModel.uploadImage(context, selectedImageUri!!) { uploadedUrl ->
                    if (uploadedUrl != null) {
                        // Step 2: Create Data Model with the Cloudinary URL
                        val product = ProductModel(
                            name = name,
                            price = priceValue,
                            description = description,
                            categoryId = category,
                            image = uploadedUrl
                        )

                        // Step 3: Save Model to Firebase
                        productViewModel.addProduct(product) { success, message ->
                            isUploading = false
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                            if (success) {
                                navController.popBackStack()
                            }
                        }
                    } else {
                        isUploading = false
                        Toast.makeText(context, "Failed to upload image to Cloudinary", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        ) {
            if (isUploading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp
                )
            } else {
                Text("Confirm & Upload Package")
            }
        }
    }
}