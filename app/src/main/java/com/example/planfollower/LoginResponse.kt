package com.example.planfollower

data class LoginResponse(
    val message: String,
    val token: String,
    val user: UserData)
