package com.example.planfollower.models

import com.google.gson.annotations.SerializedName

data class NotificationResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("recipient_id") val recipientId: String,
    @SerializedName("sender_id") val senderId: String,
    @SerializedName("note_id") val noteId: String,
    @SerializedName("message") val message: String,
    @SerializedName("is_read") val isRead: Boolean,
    @SerializedName("created_at") val createdAt: String
)