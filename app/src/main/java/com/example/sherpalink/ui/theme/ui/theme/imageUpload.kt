package com.example.sherpalink.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.res.painterResource
import com.example.sherpalink.viewmodel.ImageViewModel

@Composable
fun ImageUploadScreen(
    navController: NavController,
    viewModel: ImageViewModel
) {
    // Observe selected image from ViewModel
    val selectedImage by viewModel.selectedImage.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Upload Image", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        if (selectedImage != null) {
            Image(
                painter = painterResource(selectedImage!!),
                contentDescription = "Selected Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )
        } else {
            Text("No image selected", style = MaterialTheme.typography.bodyLarge)
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Upload button
        Button(onClick = {
            // TODO: Implement upload logic
        }, enabled = selectedImage != null) {
            Text("Upload")
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Clear button
        Button(onClick = { viewModel.clearImage() }, enabled = selectedImage != null) {
            Text("Clear")
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Back button
        Button(onClick = { navController.popBackStack() }) {
            Text("Back")
        }
    }
}
