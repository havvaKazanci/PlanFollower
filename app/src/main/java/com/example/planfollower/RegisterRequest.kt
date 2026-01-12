package com.example.planfollower

data class RegisterRequest(
    val name: String,
    val surname: String,
    val email: String,
    val password: String
)