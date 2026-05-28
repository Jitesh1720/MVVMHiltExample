package com.example.demokotlinapp.network

import com.example.demokotlinapp.model.User
import retrofit2.http.GET

interface ReqResService {
    @GET("users")
    suspend fun getUsers(): List<User>
}
