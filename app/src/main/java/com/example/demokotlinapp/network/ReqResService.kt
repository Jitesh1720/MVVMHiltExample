package com.example.demokotlinapp.network

import com.example.demokotlinapp.model.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ReqResService {
    @GET("api/users")
    suspend fun getUsers(@Query("page") page: Int): UserResponse
}
