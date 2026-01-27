package com.example.planfollower.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.planfollower.models.NotificationResponse
import com.example.planfollower.repository.NotificationRepository
import kotlinx.coroutines.launch

class NotificationViewModel(private val repository: NotificationRepository) : ViewModel() {

    // Observable data for the UI
    val unreadNotifications = MutableLiveData<List<NotificationResponse>>()
    val unreadCount = MutableLiveData<Int>()

    // Fetch notifications and update state
    fun fetchNotifications(token: String) {
        viewModelScope.launch {
            val response = repository.getNotifications(token)
            if (response.isSuccessful) {
                unreadNotifications.postValue(response.body())
                unreadCount.postValue(response.body()?.size ?: 0)
            }
        }
    }
}