package com.example.submisionintermediate.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.submisionintermediate.data.pref.UserPreference
import com.example.submisionintermediate.data.response.AddStoryResponse
import com.example.submisionintermediate.data.response.AllStoryResponse
import com.example.submisionintermediate.data.response.LoginResponse
import com.example.submisionintermediate.data.response.RegisterResponse
import com.example.submisionintermediate.data.retrofit.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.Dispatcher
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UserRepository private constructor(
    private val userPreference: UserPreference,
    val apiService: ApiService
) {
    suspend fun simpanPosisi(kode: String){
        userPreference.simpanPosisi(kode)
    }
    fun getSesion(): Flow<String?> {
        return userPreference.getSesion()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    suspend fun login(email: String, password: String): Flow<Result<LoginResponse>> = flow{
        try{
            val response=apiService.login(email,password)
            emit(Result.success(response))
        }catch (e:Exception){
            e.printStackTrace()
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun register(name: String, email: String, password: String): RegisterResponse = try {
        apiService.register(name, email, password)
    } catch (e: Exception) {
        throw e
    }

    suspend fun tambahStory(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody
    ): AddStoryResponse {
        return apiService.addStory(token, file, description)
    }



    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userPreference, apiService)
            }.also { instance = it }
    }
}