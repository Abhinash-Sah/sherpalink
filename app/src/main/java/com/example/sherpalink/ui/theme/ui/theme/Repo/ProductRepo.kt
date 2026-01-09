package com.example.sherpalink.repository

import android.content.Context
import android.net.Uri
import com.example.sherpalink.ProductModel

interface ProductRepo {
    fun addProduct(model: ProductModel, callback: (Boolean, String) -> Unit)
    fun deleteProduct(productId: String, callback: (Boolean, String) -> Unit)
    fun editProduct(model: ProductModel, callback: (Boolean, String) -> Unit)
    fun getAllProduct(callback: (Boolean, String, List<ProductModel>?) -> Unit)
    fun getProductById(productId: String, callback: (Boolean, String, ProductModel?) -> Unit)
    fun uploadImage(context: Context,imageUri: Uri,callback: (String?) -> Unit
    )
    fun getFileNameFromUri(context: Context,uri: Uri): String?
}
