package com.example.sherpalink

    data class UserModel(
        val userId: String = "",
        val firstName: String = "",
        val lastName: String = "",
        val email: String = "",
        val password: String = "",
        val dob: String = "",
        val gender: String = "",
        val role: String = ""
    ) {

        fun toMap(): Map<String, Any?> {
            return mapOf(
                "userId" to userId,
                "firstName" to firstName,
                "lastName" to lastName,
                "email" to email,
                "dob" to dob,
                "gender" to gender,
                "role" to role
            )
        }
    }

