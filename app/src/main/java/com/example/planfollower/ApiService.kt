package com.example.planfollower


import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    // POST http://10.0.2.2:3000/api/auth/register
    @POST("api/auth/register")
    suspend fun registerUser(@Body request: RegisterRequest): Response<RegisterResponse>

    // POST http://10.0.2.2:3000/api/auth/login
    @POST("api/auth/login")
    suspend fun loginUser(@Body request: LoginRequest): Response<LoginResponse>

    @POST("api/notes")
    suspend fun createNote(
        @Header("Authorization") token: String, // Bearer token goes here
        @Body request: NoteRequest
    ): Response<NoteResponse>


    @GET("api/notes")
    suspend fun getUserNotes(
        @Header("Authorization") token: String
    ): Response<List<NoteDetail>> // returns a list of previously defined NoteDetail model
}