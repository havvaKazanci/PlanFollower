package com.example.planfollower.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.planfollower.api.RetrofitClient
import com.example.planfollower.models.NoteDetail
import com.example.planfollower.models.NoteRequest
import com.example.planfollower.models.ShareRequest
import kotlinx.coroutines.launch

class NotesViewModel: ViewModel() {

    private val _noteList = MutableLiveData<List<NoteDetail>>()
    val noteList: LiveData<List<NoteDetail>> get() = _noteList

    private val _shareResult = MutableLiveData<String>()
    val shareResult: LiveData<String> = _shareResult

    fun fetchNotes(token: String,search: String? = null) {
        val authHeader = "Bearer $token" //bearer for middleware


        viewModelScope.launch {
            try {
                //backend Retrofit setting
                val response = RetrofitClient.instance.getUserNotes(authHeader,search)

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


    fun shareNote(token: String, noteId: String, email: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.instance.shareNote("Bearer $token", noteId,
                    ShareRequest(email)
                )
                when (response.code()) {
                    200 -> _shareResult.value = "Note successfully shared."
                    404 -> _shareResult.value = "Error: User cannot find."
                    403 -> _shareResult.value = "You are not able to share this note."
                    else -> _shareResult.value = "Error occured: ${response.code()}"
                }
            } catch (e: Exception) {
                _shareResult.value = "Connection Error: ${e.message}"
            }
        }
    }
}