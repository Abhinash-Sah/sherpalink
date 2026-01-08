package com.example.sherpalink.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.sherpalink.ProductModel
import com.example.sherpalink.viewmodel.ProductViewModel
import java.text.NumberFormat

@Composable
fun TourPackageScreen(
    navController: NavController,
    productViewModel: ProductViewModel
) {
    val products by productViewModel.allProducts.observeAsState(emptyList())
    val loading by productViewModel.loading.observeAsState(false)


    LaunchedEffect(Unit) {
        productViewModel.getAllProduct()
    }

    Box(modifier = Modifier.fillMaxSize()) {

        when {
            loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            products.isEmpty() -> {
                Text(
                    text = "No tour packages available yet",
                    modifier = Modifier.align(Alignment.Center),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            else -> {
                LazyColumn(contentPadding = PaddingValues(16.dp)) {
                    items(products) { product ->
                        TourPackageItem(product, navController)
                    }
                }
            }
        }
    }
}

@Composable
fun TourPackageItem(product: ProductModel, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
            .clickable {
                // Navigate to full-screen image, encode URL for safe passing
                navController.navigate("full_image/${java.net.URLEncoder.encode(product.image, "utf-8")}")
            },
        shape = RoundedCornerShape(12.dp)
    ) {
        Column {

            if (product.image.isNotEmpty()) {
                AsyncImage(
                    model = product.image,
                    contentDescription = product.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    contentScale = ContentScale.Crop
                )
            }

            Column(modifier = Modifier.padding(12.dp)) {
                Text(product.name, style = MaterialTheme.typography.titleMedium)
                val formattedPrice = NumberFormat.getInstance().format(product.price)
                Text("Rs. $formattedPrice", style = MaterialTheme.typography.bodyMedium)
                Text(
                    product.description,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
