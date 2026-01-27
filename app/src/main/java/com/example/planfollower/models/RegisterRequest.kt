package com.example.planfollower.models

data class RegisterRequest(
    val name: String,
    val surname: String,
    val email: String,
    val password: String
)