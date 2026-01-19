package com.example.planfollower

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.planfollower.api.RetrofitClient
import kotlinx.coroutines.launch


class NotesViewModel: ViewModel() {

    private val _noteList = MutableLiveData<List<NoteDetail>>()
    val noteList: LiveData<List<NoteDetail>> get() = _noteList

    fun fetchNotes(token: String) {
        val authHeader = "Bearer $token" //bearer for middleware


        viewModelScope.launch {
            try {
                //backend Retrofit setting
                val response = RetrofitClient.instance.getUserNotes(authHeader)

                if (response.isSuccessful) {
                    // setting coming data
                    _noteList.value = response.body() ?: emptyList()
                } else {
                    Log.e("PlanFollower", "Fetch Notes Error: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("PlanFollower", "Connection Error: ${e.message}")
            }
        }
    }


    fun deleteNote(token: String, noteId: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.instance.deleteNote("Bearer $token", noteId)
                if (response.isSuccessful) {
                    fetchNotes(token)
                }
            } catch (e: Exception) {
                Log.e("PlanFollower", "Delete error: ${e.message}")
            }
        }
    }


    fun updateNote(token: String, noteId: String, title: String, content: String) {
        viewModelScope.launch {
            try {
                val request = NoteRequest(title, content)
                val response = RetrofitClient.instance.updateNote("Bearer $token", noteId, request)
                if (response.isSuccessful) {
                    //show new list when updating successful
                    fetchNotes(token)
                }
            } catch (e: Exception) {
                Log.e("PlanFollower", "Update error: ${e.message}")
            }
        }
    }
}