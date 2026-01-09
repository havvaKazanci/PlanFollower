package com.example.planfollower

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class NotesViewModel: ViewModel() {

    val noteList = MutableLiveData<List<Note>>(emptyList())

    fun addNote(note: Note) {
        val newList = noteList.value.orEmpty().toMutableList()
        newList.add(note)
        noteList.value = newList
    }
}