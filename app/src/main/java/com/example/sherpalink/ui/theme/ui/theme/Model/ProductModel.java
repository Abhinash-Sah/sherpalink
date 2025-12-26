package com.example.sherpalink

data class ProductModel(
        var productId: String = "",
        var name: String = "",          // Tour name
        var price: Double = 0.0,        // Tour price
        var description: String = "",   // Tour details
        var categoryId: String = "",    // Trek / Tour / Region
        var image: String = ""          // Image URL
) {

    fun toMap(): Map<String, Any?> {
        return mapOf(
                "name" to name,
                "price" to price,
                "description" to description,
                "categoryId" to categoryId,
                "image" to image
        )
    }
}
