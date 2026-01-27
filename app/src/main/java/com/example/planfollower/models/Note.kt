package com.example.planfollower.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Note (val title: String, val noteDetail: String) : Parcelable