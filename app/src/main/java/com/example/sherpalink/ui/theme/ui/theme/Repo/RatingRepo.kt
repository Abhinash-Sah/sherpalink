package com.example.sherpalink.ui.theme.ui.theme.Repo

import com.example.sherpalink.ui.theme.ui.theme.Model.ReviewModel

interface ReviewRepo {
    fun submitReview(review: ReviewModel, callback: (Boolean, String) -> Unit)
    fun getReviewsForGuide(guideId: String, callback: (Boolean, List<ReviewModel>) -> Unit)
}