package com.example.sherpalink.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sherpalink.ProductModel
import com.example.sherpalink.repository.ProductRepo

class ProductViewModel(private val repo: ProductRepo) : ViewModel() {

    // Single product
    private val _product = MutableLiveData<ProductModel?>()
    val product: LiveData<ProductModel?> get() = _product

    // All products
    private val _allProducts = MutableLiveData<List<ProductModel>>()
    val allProducts: LiveData<List<ProductModel>> get() = _allProducts

    // Loading state
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    // Add product
    fun addProduct(model: ProductModel, callback: (Boolean, String) -> Unit) {
        repo.addProduct(model, callback)
    }

    // Delete product
    fun deleteProduct(productId: String, callback: (Boolean, String) -> Unit) {
        repo.deleteProduct(productId, callback)
    }

    // Edit product
    fun editProduct(model: ProductModel, callback: (Boolean, String) -> Unit) {
        repo.editProduct(model, callback)
    }

    // Get all products
    fun getAllProduct() {
        _loading.postValue(true)
        repo.getAllProduct { success, _, data ->
            _loading.postValue(false)
            if (success) _allProducts.postValue(data) else _allProducts.postValue(emptyList())
        }
    }

    // Get product by ID
    fun getProductById(productId: String) {
        repo.getProductById(productId) { success, _, data ->
            _product.postValue(if (success) data else null)
        }
    }
}
