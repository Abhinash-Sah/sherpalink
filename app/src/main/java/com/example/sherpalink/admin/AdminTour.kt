package com.example.sherpalink.screens.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
@Composable
fun AdminTourListScreen(navController: androidx.navigation.NavHostController, productViewModel: com.example.sherpalink.viewmodel.ProductViewModel) {
    val products by productViewModel.allProducts.observeAsState(emptyList())

    LaunchedEffect (Unit) { productViewModel.getAllProduct() }

    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        items(products) { product ->
            Card(modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(product.name, style = MaterialTheme.typography.titleMedium)
                    Text("Rs. ${product.price.toInt()}")

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        TextButton(onClick = { navController.navigate("admin_edit/${product.productId}") }) {
                            Text("Edit")
                        }
                        TextButton(onClick = { productViewModel.deleteProduct(product.productId) { _, _ -> productViewModel.getAllProduct() } }) {
                            Text("Delete", color = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            }
        }
    }
}

