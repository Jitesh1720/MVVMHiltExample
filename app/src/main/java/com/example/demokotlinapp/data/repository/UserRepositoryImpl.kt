package com.example.demokotlinapp.data.repository

import com.example.demokotlinapp.model.User
import com.example.demokotlinapp.network.ReqResService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val apiService: ReqResService
) : UserRepository {
    override suspend fun getUsers(): List<User> {
        return apiService.getUsers()
    }
}
