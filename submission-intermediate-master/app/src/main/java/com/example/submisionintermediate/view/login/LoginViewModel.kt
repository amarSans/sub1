package com.example.submisionintermediate.view.login

import android.app.appsearch.AppSearchSession
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.submisionintermediate.data.UserRepository
import com.example.submisionintermediate.data.response.LoginResponse
import kotlinx.coroutines.launch


class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun saveSession(kode: String) {
        viewModelScope.launch {
            userRepository.simpanPosisi(kode)
        }
    }

    suspend fun login(
        email: String,
        password: String
    ): kotlinx.coroutines.flow.Flow<Result<LoginResponse>> = userRepository.login(email, password)


}