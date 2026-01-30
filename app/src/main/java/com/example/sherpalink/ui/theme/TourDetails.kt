package com.example.sherpalink.screens

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.example.sherpalink.viewmodel.ProductViewModel


@Composable
fun TourDetailsScreenSafe(
    navController: NavHostController,
    viewModel: ProductViewModel,
    productId: String
) {
    val product by viewModel.product.observeAsState()

    LaunchedEffect(productId) {
        if (productId.isNotEmpty()) viewModel.getProductById(productId)
    }

    Box(modifier = Modifier.fillMaxSize()) {

        product?.let { tour ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 80.dp)
            ) {
                coil3.compose.AsyncImage(
                    model = tour.image.takeIf { it.isNotEmpty() },
                    contentDescription = tour.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    contentScale = ContentScale.Crop
                )

                Column(modifier = Modifier.padding(16.dp)) {
                    Text(tour.name, style = MaterialTheme.typography.headlineMedium)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "Rs. ${tour.price.toInt()}",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Description", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(tour.description, style = MaterialTheme.typography.bodyLarge)
                }
            }

            Button(
                onClick = {
                    val safeId = Uri.encode(tour.productId)
                    val safeName = Uri.encode(tour.name)
                    val bookingType = Uri.encode("Tour") // or "Guide", etc.

                    navController.navigate(
                        "registration_form/$safeId/$safeName/$bookingType"
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3F6ED5))
            ) {
                Text("Book Now", color = Color.White)
            }


        } ?: Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }

        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .padding(16.dp)
                .size(40.dp)
                .align(Alignment.TopStart)
                .background(Color.White.copy(alpha = 0.7f), shape = MaterialTheme.shapes.small)
        ) {
            Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back", tint = Color.Black)
        }
    }
}