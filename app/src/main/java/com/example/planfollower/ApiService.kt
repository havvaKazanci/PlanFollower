package com.example.planfollower


import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

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
        @Header("Authorization") token: String,
        @Query("search") query: String? = null
    ): Response<List<NoteDetail>> // returns a list of previously defined NoteDetail model


    @DELETE("api/notes/{id}")
    suspend fun deleteNote(
        @Header("Authorization") token: String,
        @Path("id") noteId: String
    ): Response<ResponseBody> // ResponseBody used for only success message


    @PUT("api/notes/{id}")
    suspend fun updateNote(
        @Header("Authorization") token: String,
        @Path("id") noteId: String,
        @Body request: NoteRequest //data model for notes
    ): Response<NoteDetail>


    @POST("api/notes/{id}/share")
    suspend fun shareNote(
        @Header("Authorization") token: String,
        @Path("id") noteId: String,
        @Body request: ShareRequest
    ): Response<Unit>
}