package com.example.sherpalink.screens

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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.sherpalink.ProductModel
import com.example.sherpalink.viewmodel.ProductViewModel
import java.text.NumberFormat
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.sherpalink.R
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource



@Composable
fun TourPackageScreen(
    navController: NavController,
    productViewModel: Any
) {
    val products = when (productViewModel) {
        is ProductViewModel -> productViewModel.allProducts.observeAsState(emptyList()).value
        is PreviewProductViewModel -> productViewModel.allProducts.value
        else -> emptyList()
    }

    val loading = when (productViewModel) {
        is ProductViewModel -> productViewModel.loading.observeAsState(false).value
        is PreviewProductViewModel -> productViewModel.loading.value
        else -> false
    }

    // Load products if using real ViewModel
    if (productViewModel is ProductViewModel) {
        LaunchedEffect(Unit) {
            productViewModel.getAllProduct()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            products.isEmpty() -> Text(
                text = "No tour packages available yet",
                modifier = Modifier.align(Alignment.Center),
                style = MaterialTheme.typography.bodyMedium
            )
            else -> LazyColumn(contentPadding = PaddingValues(16.dp)) {
                items(products) { product ->
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

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
            .clickable { expanded = !expanded },
        shape = RoundedCornerShape(12.dp)
    ) {
        Column {
            if (isPreview && product.previewImage != null) {
                Image(
                    painter = painterResource(product.previewImage!!),
                    contentDescription = product.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    contentScale = ContentScale.Crop
                )
            } else {
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
                Text("Rs. ${product.price.toInt()}")
                Text(
                    product.description,
                    maxLines = if (expanded) Int.MAX_VALUE else 2,
                    overflow = if (expanded) TextOverflow.Visible else TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}




class PreviewProductViewModel {
    val allProducts = mutableStateOf(
        listOf(
            ProductModel(
                productId = "1",
                name = "Everest Base Camp Trek",
                price = 25000.0,
                description = "An amazing trek to the base of the world's highest mountain.",
                categoryId = "Trek",
                previewImage = R.drawable.everest
            ),
            ProductModel(
                productId = "2",
                name = "Pokhara Sightseeing",
                price = 15000.0,
                description = "Explore the beautiful lakes and culture of Pokhara.",
                categoryId = "Tour",
                previewImage = R.drawable.pokhara
            ),
            ProductModel(
                productId = "3",
                name = "Chitwan Jungle Safari",
                price = 18000.0,
                description = "Experience the wildlife adventure in Chitwan National Park.",
                categoryId = "Tour",
                previewImage = R.drawable.chitwan
            )
        )
    )
    val loading = mutableStateOf(false)
}

@Preview(showBackground = true)
@Composable
fun TourPackageScreenPreview() {
    val navController = rememberNavController()
    val viewModel = PreviewProductViewModel()

    TourPackageScreen(
        navController = navController,
        productViewModel = viewModel
    )
}
