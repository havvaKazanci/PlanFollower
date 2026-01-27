package com.example.planfollower.repository

import com.example.planfollower.api.NotificationService
import com.example.planfollower.models.NotificationResponse
import retrofit2.Response

class NotificationRepository(private val apiService: NotificationService) {

    // Fetch notifications from backend
    suspend fun getNotifications(token: String): Response<List<NotificationResponse>> {
        return apiService.getUnreadNotifications("Bearer $token")
    }

    // Update notification status in database
    suspend fun markNotificationRead(token: String, id: Int): Response<Unit> {
        return apiService.markAsRead("Bearer $token", id)
    }
}