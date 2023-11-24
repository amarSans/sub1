package com.example.submisionintermediate.view.daftar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.submisionintermediate.data.UserRepository
import com.example.submisionintermediate.data.response.RegisterResponse

class SignupViewModel  constructor(private val userRepository: UserRepository) : ViewModel() {
    suspend fun register(name: String, email: String, password: String): RegisterResponse {
        return userRepository.register(name, email, password)
    }

}