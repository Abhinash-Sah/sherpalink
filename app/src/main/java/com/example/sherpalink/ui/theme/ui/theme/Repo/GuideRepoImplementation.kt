package com.example.sherpalink.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.example.sherpalink.model.GuideModel
import com.google.firebase.database.FirebaseDatabase

class GuideRepoImplementation(val context: Context) : GuideRepo {

    private val database = FirebaseDatabase.getInstance().getReference("guides")

    override fun addGuide(guide: GuideModel, imageUri: Uri?, callback: (Boolean, String) -> Unit) {
        if (imageUri == null) {
            callback(false, "Image is required")
            return
        }

        // 1. Upload to Cloudinary First
        val requestId = MediaManager.get().upload(imageUri)
            .option("folder", "guides")
            .callback(object : UploadCallback {
                override fun onStart(requestId: String?) {
                    Log.d("Upload", "Cloudinary upload started...")
                }

                override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) {}

                override fun onSuccess(requestId: String?, resultData: Map<*, *>) {
                    val imageUrl = resultData["secure_url"] as String
                    Log.d("Upload", "Cloudinary Success: $imageUrl")

                    // 2. Once image is uploaded, save to Firebase
                    val guideId = database.push().key ?: ""
                    val finalGuide = guide.copy(guideId = guideId, imageUrl = imageUrl)

                    database.child(guideId).setValue(finalGuide)
                        .addOnSuccessListener {
                            Log.d("Upload", "Firebase Success")
                            callback(true, "Guide added successfully!")
                        }
                        .addOnFailureListener {
                            Log.e("Upload", "Firebase Failed: ${it.message}")
                            callback(false, "Firebase Error: ${it.message}")
                        }
                }

                override fun onError(requestId: String?, error: ErrorInfo?) {
                    Log.e("Upload", "Cloudinary Error: ${error?.description}")
                    callback(false, "Image upload failed: ${error?.description}")
                }

                override fun onReschedule(requestId: String?, error: ErrorInfo?) {}
            }).dispatch()
    }

    override fun getAllGuides(callback: (List<GuideModel>) -> Unit) {
        database.addValueEventListener(object : com.google.firebase.database.ValueEventListener {
            override fun onDataChange(snapshot: com.google.firebase.database.DataSnapshot) {
                val list = mutableListOf<GuideModel>()
                snapshot.children.forEach {
                    val guide = it.getValue(GuideModel::class.java)
                    guide?.let { list.add(it) }
                }
                callback(list)
            }
            override fun onCancelled(error: com.google.firebase.database.DatabaseError) {
                Log.e("Firebase", "Load failed: ${error.message}")
            }
        })
    }

    override fun deleteGuide(guideId: String, callback: (Boolean, String) -> Unit) {
        database.child(guideId).removeValue().addOnCompleteListener {
            if (it.isSuccessful) callback(true, "Guide deleted")
            else callback(false, "Delete failed")
        }
    }
}