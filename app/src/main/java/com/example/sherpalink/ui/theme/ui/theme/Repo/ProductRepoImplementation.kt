package com.example.sherpalink.repository

import com.example.sherpalink.ProductModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProductRepoImplementation : ProductRepo {

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val ref: DatabaseReference = database.getReference("products")

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
        ref.child(model.productId).updateChildren(model.toMap()).addOnCompleteListener {
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
                        if (product != null) allProducts.add(product)
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
}
