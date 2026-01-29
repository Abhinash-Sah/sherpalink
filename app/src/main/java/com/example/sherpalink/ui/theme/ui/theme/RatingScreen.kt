package com.example.sherpalink.ui.theme

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sherpalink.ui.theme.ui.theme.Model.ReviewModel
import com.example.sherpalink.ui.theme.ui.theme.ViewModel.ReviewViewModel
import com.example.sherpalink.viewmodel.UserViewModel

@Composable
fun RatingsScreen(
    reviewViewModel: ReviewViewModel,
    userViewModel: UserViewModel,
    guideId: String = "sample_guide_123" // Pass this from the booking/guide details
) {
    val context = LocalContext.current
    var rating by remember { mutableStateOf(0) }
    var comment by remember { mutableStateOf("") }

    // State to hold the list of reviews fetched from Firebase
    var reviewList by remember { mutableStateOf<List<ReviewModel>>(emptyList()) }
    var isLoadingReviews by remember { mutableStateOf(true) }

    // Fetch reviews when the screen loads
    LaunchedEffect(Unit) {
        reviewViewModel.getReviewsForGuide(guideId) { success, list ->
            reviewList = list
            isLoadingReviews = false
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // --- SECTION 1: HEADER & INPUT ---
        item {
            Text(
                text = "Rate Your Experience",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1B263B)
            )

            Card(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("How was your guide?", fontWeight = FontWeight.SemiBold)

                    // Star Input
                    Row(modifier = Modifier.padding(vertical = 8.dp)) {
                        repeat(5) { index ->
                            val starLevel = index + 1
                            Icon(
                                imageVector = if (starLevel <= rating) Icons.Filled.Star else Icons.Outlined.Star,
                                contentDescription = null,
                                tint = if (starLevel <= rating) Color(0xFFFFD700) else Color.LightGray,
                                modifier = Modifier
                                    .size(36.dp)
                                    .clickable { rating = starLevel }
                            )
                        }
                    }

                    OutlinedTextField(
                        value = comment,
                        onValueChange = { comment = it },
                        placeholder = { Text("Write your review here...") },
                        modifier = Modifier.fillMaxWidth().height(100.dp),
                        shape = RoundedCornerShape(8.dp)
                    )

                    Button(
                        onClick = {
                            if (rating == 0) {
                                Toast.makeText(context, "Please select a star rating", Toast.LENGTH_SHORT).show()
                                return@Button
                            }

                            val user = userViewModel.user
                            val newReview = ReviewModel(
                                guideId = guideId,
                                userId = user?.userId ?: "anonymous",
                                userName = "${user?.firstName ?: "Guest"} ${user?.lastName ?: ""}",
                                rating = rating,
                                comment = comment
                            )

                            reviewViewModel.postReview(newReview) { success, msg ->
                                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                                if (success) {
                                    rating = 0
                                    comment = ""
                                    // Refresh list after posting
                                    reviewViewModel.getReviewsForGuide(guideId) { _, list -> reviewList = list }
                                }
                            }
                        },
                        modifier = Modifier.align(Alignment.End).padding(top = 8.dp),
                        enabled = !reviewViewModel.isSubmitting,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
                    ) {
                        if (reviewViewModel.isSubmitting) {
                            CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
                        } else {
                            Text("Submit Review")
                        }
                    }
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
            Text(text = "What others said", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }

        // --- SECTION 2: THE LIST OF REVIEWS ---
        if (isLoadingReviews) {
            item {
                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color(0xFF2E7D32))
                }
            }
        } else if (reviewList.isEmpty()) {
            item {
                Text("No reviews yet. Be the first to rate!", color = Color.Gray, modifier = Modifier.padding(16.dp))
            }
        } else {
            items(reviewList) { review ->
                ReviewItem(review)
            }
        }
    }
}

@Composable
fun ReviewItem(review: ReviewModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                repeat(5) { index ->
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = null,
                        tint = if (index < review.rating) Color(0xFFFFD700) else Color.LightGray,
                        modifier = Modifier.size(16.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = review.userName, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
            if (review.comment.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = review.comment, fontSize = 14.sp, color = Color.DarkGray)
            }
        }
    }
}