package com.example.sherpalink.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sherpalink.ProductModel
import com.example.sherpalink.repository.ProductRepo

class ProductViewModel(private val repo: ProductRepo) : ViewModel() {

    private val _product = MutableLiveData<ProductModel?>()
    val product: MutableLiveData<ProductModel?> get() = _product

    private val _allProducts = MutableLiveData<List<ProductModel>?>()
    val allProducts: MutableLiveData<List<ProductModel>?> get() = _allProducts

    private val _loading = MutableLiveData<Boolean>()
    val loading: MutableLiveData<Boolean> get() = _loading

    fun addProduct(model: ProductModel, callback: (Boolean, String) -> Unit) {
        repo.addProduct(model, callback)
    }

    fun deleteProduct(productId: String, callback: (Boolean, String) -> Unit) {
        repo.deleteProduct(productId, callback)
    }

    fun editProduct(model: ProductModel, callback: (Boolean, String) -> Unit) {
        repo.editProduct(model, callback)
    }

    fun getAllProduct() {
        _loading.postValue(true)
        repo.getAllProduct { success, message, data ->
            _loading.postValue(false)
            if (success) _allProducts.postValue(data) else _allProducts.postValue(emptyList())
        }
    }

    fun getProductById(productId: String) {
        repo.getProductById(productId) { success, _, data ->
            _product.postValue(if (success) data else null)
        }
    }

    //fun uploadImage(context: Context, imageUri: Uri, callback: (String?) -> Unit) {
        // Only if your repo has uploadImage method
       // repo.uploadImage(context, imageUri, callback)
    //}
}
