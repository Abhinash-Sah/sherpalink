package com.example.sherpalink.repository

import com.example.sherpalink.model.NotificationModel
import com.google.firebase.database.*
class NotificationRepoImplementation(
    private val userId: String
) : NotificationRepo {

    private val db = FirebaseDatabase.getInstance()
        .getReference("notifications")
        .child(userId)

    override fun getNotifications(onResult: (List<NotificationModel>) -> Unit) {
        db.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = snapshot.children.mapNotNull {
                    it.getValue(NotificationModel::class.java)
                }.sortedByDescending { it.time }

                onResult(list)
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    override fun addNotification(notification: NotificationModel) {
        val id = db.push().key ?: return
        db.child(id).setValue(notification.copy(id = id))
    }
}
