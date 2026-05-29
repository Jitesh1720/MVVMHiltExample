package com.example.demokotlinapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.demokotlinapp.data.repository.UserRepository
import com.example.demokotlinapp.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class UserUiState {
    object Loading : UserUiState()
    data class Success(val users: List<User>) : UserUiState()
    data class Error(val message: String) : UserUiState()
}

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableLiveData<UserUiState>()
    val uiState: LiveData<UserUiState> get() = _uiState

    fun fetchUsers() {
        _uiState.value = UserUiState.Loading

        viewModelScope.launch {
            try {
                val response = userRepository.getUsers()
                _uiState.value = UserUiState.Success(response)
            } catch (e: Exception) {
                _uiState.value = UserUiState.Error(e.localizedMessage ?: "Failed to fetch users")
            }
        }
    }
}
