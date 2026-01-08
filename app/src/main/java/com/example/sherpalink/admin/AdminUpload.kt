package com.example.sherpalink.screens.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AdminTourUploadScreen(navController: androidx.navigation.NavHostController, productViewModel: com.example.sherpalink.viewmodel.ProductViewModel) {
    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(12.dp)) {

        Text("Add Tour Package", style = MaterialTheme.typography.headlineSmall)

        OutlinedTextField(name, { name = it }, label = { Text("Tour Name") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(price, { price = it }, label = { Text("Price") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(category, { category = it }, label = { Text("Category") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(imageUrl, { imageUrl = it }, label = { Text("Image URL") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(description, { description = it }, label = { Text("Description") }, modifier = Modifier.fillMaxWidth(), minLines = 3)

        Button(modifier = Modifier.fillMaxWidth(), onClick = {
            val product = com.example.sherpalink.ProductModel(
                productId = java.util.UUID.randomUUID().toString(),
                name = name,
                price = price.toDoubleOrNull() ?: 0.0,
                description = description,
                categoryId = category,
                image = imageUrl
            )

            productViewModel.addProduct(product) { success, message ->
                if (success) navController.popBackStack()
            }
        }) {
            Text("Upload Tour")
        }
    }
}
