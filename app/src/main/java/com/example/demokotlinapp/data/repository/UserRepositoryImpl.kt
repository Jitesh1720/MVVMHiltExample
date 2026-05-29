package com.example.demokotlinapp.data.repository

import com.example.demokotlinapp.data.local.UserDao
import com.example.demokotlinapp.model.User
import com.example.demokotlinapp.network.ReqResService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val apiService: ReqResService,
    private val userDao: UserDao
) : UserRepository {
    override suspend fun getUsers(): List<User> {
        try {
            val networkUsers = apiService.getUsers()
            userDao.insertUsers(networkUsers)
        } catch (e: Exception) {
            // Fallback to database
        }
        
        val localUsers = userDao.getAllUsers()
        if (localUsers.isEmpty()) {
            throw Exception("No data available. Please check your internet connection.")
        }
        return localUsers
    }
}
