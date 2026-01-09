package com.example.sherpalink.screens

import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.sherpalink.ProductModel
import com.example.sherpalink.R
import com.example.sherpalink.viewmodel.ProductViewModel

@Composable
fun TourPackageScreen(navController: NavController, productViewModel: ProductViewModel) {
    val products by productViewModel.allProducts.observeAsState(emptyList())
    val loading by productViewModel.loading.observeAsState(false)

    LaunchedEffect(Unit) { productViewModel.getAllProduct() }

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            products.isEmpty() -> Text("No tour packages available", modifier = Modifier.align(Alignment.Center))
            else -> LazyColumn(contentPadding = PaddingValues(16.dp)) {
                items(products, key = { it.productId }) { product ->
                    TourPackageItem(product, navController)
                }
            }
        }
    }
}

@Composable
fun TourPackageItem(product: ProductModel, navController: NavController) {
    var expanded by remember { mutableStateOf(false) }
    val isPreview = LocalInspectionMode.current
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
            .clickable { expanded = !expanded },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.animateContentSize()) {
            Box(modifier = Modifier.fillMaxWidth().height(200.dp)) {
                if (isPreview && product.previewImage != null) {
                    Image(
                        painter = painterResource(product.previewImage!!),
                        contentDescription = product.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    AsyncImage(
                        model = product.image.takeIf { it.isNotEmpty() },
                        contentDescription = product.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(product.name, style = MaterialTheme.typography.titleLarge)
                    Text("Rs. ${product.price.toInt()}", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    product.description,
                    maxLines = if (expanded) Int.MAX_VALUE else 2,
                    overflow = if (expanded) TextOverflow.Visible else TextOverflow.Ellipsis
                )

                if (expanded) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = {
                            if (product.productId.isNotEmpty()) {
                                navController.navigate("tour_details/${product.productId}")
                            } else Toast.makeText(context, "ID not found", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.align(Alignment.End)
                    ) { Text("View Details") }
                }
            }
        }
    }
}
