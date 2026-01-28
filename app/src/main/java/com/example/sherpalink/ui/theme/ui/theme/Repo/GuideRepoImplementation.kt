package com.example.sherpalink.repository

import android.content.Context
import android.net.Uri
import com.cloudinary.Cloudinary
import com.cloudinary.utils.ObjectUtils
import com.example.sherpalink.model.GuideModel
import com.google.firebase.database.*
import java.util.concurrent.Executors

class GuideRepoImplementation(
    private val context: Context
) : GuideRepo {

    private val database: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("guides")

    // Cloudinary config
    private val cloudinary = Cloudinary(
        mapOf(
            "cloud_name" to "YOUR_CLOUD_NAME",
            "api_key" to "YOUR_API_KEY",
            "api_secret" to "YOUR_API_SECRET"
        )
    )

    private val executor = Executors.newSingleThreadExecutor()

    override fun addGuide(
        guide: GuideModel,
        imageUri: Uri?,
        callback: (Boolean, String) -> Unit
    ) {
        val guideId = database.push().key
        if (guideId == null) {
            callback(false, "Failed to generate guide ID")
            return
        }

        if (imageUri != null) {
            executor.execute {
                try {
                    val inputStream =
                        context.contentResolver.openInputStream(imageUri)
                            ?: throw Exception("Cannot open image")

                    val result = cloudinary.uploader().upload(
                        inputStream,
                        ObjectUtils.asMap("folder", "guides")
                    )

                    val imageUrl = result["secure_url"] as String

                    val updatedGuide = guide.copy(
                        guideId = guideId,
                        imageUrl = imageUrl
                    )

                    database.child(guideId).setValue(updatedGuide)
                        .addOnSuccessListener {
                            callback(true, "Guide added successfully")
                        }
                        .addOnFailureListener {
                            callback(false, it.message ?: "Database error")
                        }

                } catch (e: Exception) {
                    callback(false, e.message ?: "Image upload failed")
                }
            }
        } else {
            val updatedGuide = guide.copy(guideId = guideId)
            database.child(guideId).setValue(updatedGuide)
                .addOnSuccessListener {
                    callback(true, "Guide added successfully")
                }
                .addOnFailureListener {
                    callback(false, it.message ?: "Database error")
                }
        }
    }

    override fun getAllGuides(callback: (List<GuideModel>) -> Unit) {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<GuideModel>()
                for (data in snapshot.children) {
                    val guide = data.getValue(GuideModel::class.java)
                    if (guide != null) list.add(guide)
                }
                callback(list)
            }

            override fun onCancelled(error: DatabaseError) {
                callback(emptyList())
            }
        })
    }

    override fun deleteGuide(guideId: String, callback: (Boolean, String) -> Unit) {
        database.child(guideId).removeValue()
            .addOnSuccessListener {
                callback(true, "Guide deleted")
            }
            .addOnFailureListener {
                callback(false, it.message ?: "Delete failed")
            }
    }
}
