package com.example.demokotlinapp.data.repository

import com.example.demokotlinapp.model.User

interface UserRepository {
    suspend fun getUsers(): List<User>
}
