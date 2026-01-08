package com.example.sherpalink.screens.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.sherpalink.ProductModel
import com.example.sherpalink.viewmodel.ProductViewModel

@Composable
fun AdminEditTourScreen(navController: androidx.navigation.NavHostController, productViewModel: com.example.sherpalink.viewmodel.ProductViewModel, productId: String) {
    val product = productViewModel.allProducts.observeAsState(emptyList()).value.find { it.productId == productId }
    if (product == null) { Text("Product not found"); return }

    var name by remember { mutableStateOf(product.name) }
    var price by remember { mutableStateOf(product.price.toString()) }
    var category by remember { mutableStateOf(product.categoryId) }
    var imageUrl by remember { mutableStateOf(product.image ?: "") }
    var description by remember { mutableStateOf(product.description) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Edit Tour", style = MaterialTheme.typography.headlineSmall)

        OutlinedTextField(name, { name = it }, label = { Text("Name") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(price, { price = it }, label = { Text("Price") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(category, { category = it }, label = { Text("Category") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(imageUrl, { imageUrl = it }, label = { Text("Image URL") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(description, { description = it }, label = { Text("Description") }, modifier = Modifier.fillMaxWidth())

        Button(modifier = Modifier.fillMaxWidth(), onClick = {
            productViewModel.editProduct(product.copy(name = name, price = price.toDoubleOrNull() ?: 0.0, categoryId = category, image = imageUrl, description = description)) { success, _ ->
                if (success) navController.popBackStack()
            }
        }) { Text("Save Changes") }
    }
}

