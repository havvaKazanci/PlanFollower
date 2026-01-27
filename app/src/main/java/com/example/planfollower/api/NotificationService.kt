package com.example.planfollower.api

import com.example.planfollower.models.NotificationResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.Path

interface NotificationService {
    // Fetch all unread notifications
    @GET("api/notes/notifications")
    suspend fun getUnreadNotifications(
        @Header("Authorization") token: String
    ): Response<List<NotificationResponse>>

    // Mark a single notification as read
    @PATCH("api/notes/notifications/{id}/read")
    suspend fun markAsRead(
        @Header("Authorization") token: String,
        @Path("id") notificationId: Int
    ): Response<Unit>
}