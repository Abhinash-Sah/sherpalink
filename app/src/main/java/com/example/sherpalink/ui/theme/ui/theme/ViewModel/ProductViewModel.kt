package com.example.sherpalink.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.UploadCallback
import com.example.sherpalink.ProductModel
import com.example.sherpalink.repository.ProductRepo

class ProductViewModel(private val repo: ProductRepo) : ViewModel() {

    private val _product = MutableLiveData<ProductModel?>()
    val product: LiveData<ProductModel?> get() = _product

    private val _allProducts = MutableLiveData<List<ProductModel>>()
    val allProducts: LiveData<List<ProductModel>> get() = _allProducts

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

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
        repo.getAllProduct { success, _, data ->
            _loading.postValue(false)
            if (success) _allProducts.postValue(data) else _allProducts.postValue(emptyList())
        }
    }

    fun getProductById(productId: String) {
        repo.getProductById(productId) { success, _, data ->
            _product.postValue(if (success) data else null)
        }
    }
    fun uploadImage(context: Context, imageUri: Uri, callback: (String?) -> Unit) {
        try {
            com.cloudinary.android.MediaManager.get().upload(imageUri)
                .unsigned("sherpalink") // 🚨 CHANGE THIS to your real preset name
                .option("resource_type", "image")
                .callback(object : com.cloudinary.android.callback.UploadCallback {
                    override fun onStart(requestId: String?) {
                        android.util.Log.d("Cloudinary", "Upload started")
                    }
                    override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) {}

                    override fun onSuccess(requestId: String?, resultData: Map<*, *>?) {
                        val url = resultData?.get("secure_url") as? String
                        android.util.Log.d("Cloudinary", "Upload Success: $url")
                        callback(url)
                    }

                    override fun onError(requestId: String?, error: com.cloudinary.android.callback.ErrorInfo?) {
                        android.util.Log.e("Cloudinary", "Upload Error: ${error?.description}")
                        callback(null)
                    }

                    override fun onReschedule(requestId: String?, error: com.cloudinary.android.callback.ErrorInfo?) {
                        callback(null)
                    }
                }).dispatch()
        } catch (e: Exception) {
            android.util.Log.e("Cloudinary", "MediaManager not initialized: ${e.message}")
            callback(null)
        }
    }
    class Factory(private val repo: ProductRepo) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ProductViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ProductViewModel(repo) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}


