package com.example.sherpalink.ui.theme.ui.theme.Repo

import com.example.sherpalink.ui.theme.ui.theme.Model.ReviewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ReviewRepoImplementation : ReviewRepo {
    private val database = FirebaseDatabase.getInstance().getReference("reviews")

    override fun submitReview(review: ReviewModel, callback: (Boolean, String) -> Unit) {
        val id = database.push().key ?: ""
        val finalReview = review.copy(reviewId = id)

        database.child(id).setValue(finalReview)
            .addOnSuccessListener { callback(true, "Review submitted!") }
            .addOnFailureListener { callback(false, it.message ?: "Failed to submit") }
    }

    override fun getReviewsForGuide(guideId: String, callback: (Boolean, List<ReviewModel>) -> Unit) {
        val database = FirebaseDatabase.getInstance().getReference("reviews")

        // This looks for all reviews where "guideId" matches the one we want
        database.orderByChild("guideId").equalTo(guideId)
            .addValueEventListener(object : com.google.firebase.database.ValueEventListener {
                override fun onDataChange(snapshot: com.google.firebase.database.DataSnapshot) {
                    val reviews = snapshot.children.mapNotNull { it.getValue(ReviewModel::class.java) }
                    callback(true, reviews)
                }

                override fun onCancelled(error: com.google.firebase.database.DatabaseError) {
                    callback(false, emptyList())
                }
            })
    }
}