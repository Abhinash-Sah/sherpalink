package com.example.sherpalink.ui.theme.ui.theme.ViewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.sherpalink.ui.theme.ui.theme.Model.ReviewModel
import com.example.sherpalink.ui.theme.ui.theme.Repo.ReviewRepo

class ReviewViewModelFactory(private val repo: com.example.sherpalink.ui.theme.ui.theme.Repo.ReviewRepo) : androidx.lifecycle.ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReviewViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ReviewViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
class ReviewViewModel(private val repo: ReviewRepo) : ViewModel() {

    var isSubmitting by mutableStateOf(false)
        private set // Good practice: only the ViewModel can change this state

    fun postReview(review: ReviewModel, onComplete: (Boolean, String) -> Unit) {
        isSubmitting = true
        repo.submitReview(review) { success, message ->
            isSubmitting = false
            onComplete(success, message)
        }
    }
    fun getReviewsForGuide(guideId: String, onResult: (Boolean, List<ReviewModel>) -> Unit) {
        repo.getReviewsForGuide(guideId) { success, list ->
            onResult(success, list)}}
}