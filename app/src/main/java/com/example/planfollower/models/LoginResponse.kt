package com.example.planfollower.models

import com.example.planfollower.models.UserData

data class LoginResponse(
    val message: String,
    val token: String,
    val user: UserData
)