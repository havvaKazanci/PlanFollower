package com.example.planfollower.utils


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.planfollower.repository.NotificationRepository
import com.example.planfollower.viewmodels.NotificationViewModel

class ViewModelFactory(private val repository: NotificationRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotificationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NotificationViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}