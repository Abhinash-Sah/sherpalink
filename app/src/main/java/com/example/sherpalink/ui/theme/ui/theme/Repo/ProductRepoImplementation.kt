package com.example.sherpalink.repository



import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.OpenableColumns
import com.cloudinary.Cloudinary
import com.cloudinary.utils.ObjectUtils
import com.example.sherpalink.ProductModel
import com.example.sherpalink.repository.ProductRepo
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.io.InputStream
import java.util.concurrent.Executors

class ProductRepoImplementation : ProductRepo {

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val ref: DatabaseReference = database.getReference("products")

    private val cloudinary = Cloudinary(
        mapOf(
            "cloud_name" to "your_cloud_name",
            "api_key" to "your_api_key",
            "api_secret" to "your_api_secret"
        )
    )

    override fun addProduct(model: ProductModel, callback: (Boolean, String) -> Unit) {
        val id = ref.push().key.toString()
        model.productId = id
        ref.child(id).setValue(model).addOnCompleteListener {
            if (it.isSuccessful) callback(true, "Product added successfully")
            else callback(false, it.exception?.message ?: "Error adding product")
        }
    }

    override fun deleteProduct(productId: String, callback: (Boolean, String) -> Unit) {
        ref.child(productId).removeValue().addOnCompleteListener {
            if (it.isSuccessful) callback(true, "Product deleted successfully")
            else callback(false, it.exception?.message ?: "Error deleting product")
        }
    }

    override fun editProduct(model: ProductModel, callback: (Boolean, String) -> Unit) {
        ref.child(model.productId).setValue(model).addOnCompleteListener {
            if (it.isSuccessful) callback(true, "Product updated successfully")
            else callback(false, it.exception?.message ?: "Error updating product")
        }
    }

    override fun getAllProduct(callback: (Boolean, String, List<ProductModel>?) -> Unit) {
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val allProducts = mutableListOf<ProductModel>()
                if (snapshot.exists()) {
                    for (data in snapshot.children) {
                        val product = data.getValue(ProductModel::class.java)
                        if (product != null) {
                            // ✅ CRITICAL FIX: Assign the Firebase key to the productId field
                            product.productId = data.key ?: ""
                            allProducts.add(product)
                        }
                    }
                }
                callback(true, "Products fetched successfully", allProducts)
            }

            override fun onCancelled(error: DatabaseError) {
                callback(false, error.message, emptyList())
            }
        })
    }

    override fun getProductById(productId: String, callback: (Boolean, String, ProductModel?) -> Unit) {
        ref.child(productId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val product = snapshot.getValue(ProductModel::class.java)
                if (product != null) callback(true, "Product fetched", product)
                else callback(false, "Product not found", null)
            }

            override fun onCancelled(error: DatabaseError) {
                callback(false, error.message, null)
            }
        })
    }
    override fun uploadImage(context: Context, imageUri: Uri, callback: (String?) -> Unit) {
        com.cloudinary.android.MediaManager.get().upload(imageUri)
            .unsigned("sherpalink") // Ensure this matches your Cloudinary Preset
            .callback(object : com.cloudinary.android.callback.UploadCallback {
                override fun onStart(requestId: String?) {}
                override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) {}

                override fun onSuccess(requestId: String?, resultData: Map<*, *>?) {
                    val url = resultData?.get("secure_url") as? String
                    callback(url)
                }

                override fun onError(requestId: String?, error: com.cloudinary.android.callback.ErrorInfo?) {
                    android.util.Log.e("Cloudinary", "Error: ${error?.description}")
                    callback(null)
                }

                override fun onReschedule(requestId: String?, error: com.cloudinary.android.callback.ErrorInfo?) {
                    callback(null)
                }
            }).dispatch()
    }

    override fun getFileNameFromUri(context: Context, uri: Uri): String? {
        var fileName: String? = null
        val cursor: Cursor? = context.contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (nameIndex != -1) {
                    fileName = it.getString(nameIndex)
                }
            }
        }
        return fileName
    }
}