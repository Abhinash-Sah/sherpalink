package com.example.sherpalink.ui.theme.ui.theme

import com.example.sherpalink.UserModel
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

fun loginUser(
    email: String,
    password: String,
    onResult: (Boolean, String, UserModel?) -> Unit
) {
    val usersRef = FirebaseDatabase.getInstance().getReference("users")

    usersRef.orderByChild("email").equalTo(email.trim())
        .addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (userSnap in snapshot.children) {
                        val dbPassword = userSnap.child("password").getValue(String::class.java) ?: ""
                        if (dbPassword == password.trim()) {
                            val user = userSnap.getValue(UserModel::class.java)
                            onResult(true, "Login successful", user)
                            return
                        } else {
                            onResult(false, "Incorrect password", null)
                            return
                        }
                    }
                } else {
                    onResult(false, "Email not found", null)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                onResult(false, "Database error: ${error.message}", null)
            }
        })
}

