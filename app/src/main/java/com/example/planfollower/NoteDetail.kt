package com.example.planfollower

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NoteDetail(
    val id: String,
    val title: String,
    val content: String,
    val user_id: String): Parcelable
