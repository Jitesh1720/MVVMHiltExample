package com.example.demokotlinapp.data.repository

import com.example.demokotlinapp.data.local.UserDao
import com.example.demokotlinapp.model.Address
import com.example.demokotlinapp.model.Company
import com.example.demokotlinapp.model.Geo
import com.example.demokotlinapp.model.User
import com.example.demokotlinapp.network.ReqResService
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class UserRepositoryImplTest {

    private lateinit var apiService: ReqResService
    private lateinit var userDao: UserDao
    private lateinit var userRepository: UserRepositoryImpl

    private val mockUser = User(
        id = 1,
        name = "John Doe",
        username = "johndoe",
        email = "john@example.com",
        address = Address("Street", "Suite", "City", "12345", Geo("0", "0")),
        phone = "123-456-7890",
        website = "example.com",
        company = Company("Company", "Catchphrase", "BS")
    )

    @Before
    fun setup() {
        apiService = mockk()
        userDao = mockk(relaxed = true)
        userRepository = UserRepositoryImpl(apiService, userDao)
    }

    @Test
    fun `getUsers fetches from API, saves to DB, and returns from DB on success`() = runTest {
        // Arrange
        val remoteUsers = listOf(mockUser)
        val localUsers = listOf(mockUser)

        coEvery { apiService.getUsers() } returns remoteUsers
        coEvery { userDao.getAllUsers() } returns localUsers

        // Act
        val result = userRepository.getUsers()

        // Assert
        assertEquals(localUsers, result)
        coVerify { userDao.insertUsers(remoteUsers) }
        coVerify { userDao.getAllUsers() }
    }

    @Test
    fun `getUsers returns from DB when API fails`() = runTest {
        // Arrange
        val localUsers = listOf(mockUser)

        coEvery { apiService.getUsers() } throws Exception("Network Error")
        coEvery { userDao.getAllUsers() } returns localUsers

        // Act
        val result = userRepository.getUsers()

        // Assert
        assertEquals(localUsers, result)
        coVerify(exactly = 0) { userDao.insertUsers(any()) }
        coVerify { userDao.getAllUsers() }
    }

    @Test
    fun `getUsers throws exception when API fails and DB is empty`() = runTest {
        // Arrange
        coEvery { apiService.getUsers() } throws Exception("Network Error")
        coEvery { userDao.getAllUsers() } returns emptyList()

        // Act
        var exceptionThrown = false
        try {
            userRepository.getUsers()
        } catch (e: Exception) {
            exceptionThrown = true
            assertEquals("No data available. Please check your internet connection.", e.message)
        }

        // Assert
        assertTrue(exceptionThrown)
    }
}
