package com.example.demokotlinapp.data.repository

import com.example.demokotlinapp.model.UserResponse

interface UserRepository {
    suspend fun getUsers(page: Int): UserResponse
}
