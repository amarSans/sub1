package com.example.submisionintermediate.data

import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.submisionintermediate.data.database.databaseStory
import com.example.submisionintermediate.data.database.storyItem
import com.example.submisionintermediate.data.pref.UserPreference
import com.example.submisionintermediate.data.response.AddStoryResponse
import com.example.submisionintermediate.data.response.AllStoryResponse
import com.example.submisionintermediate.data.response.ListStoryItem
import com.example.submisionintermediate.data.response.LoginResponse
import com.example.submisionintermediate.data.response.RegisterResponse
import com.example.submisionintermediate.data.retrofit.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UserRepository private constructor(
    private val userPreference: UserPreference, val apiService: ApiService,private val databaseStory: databaseStory
) {
    suspend fun simpanPosisi(kode: String) {
        userPreference.simpanPosisi(kode)
    }

    fun getToken(): Flow<String?> {
        return userPreference.getUser()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    suspend fun login(email: String, password: String): Flow<Result<LoginResponse>> = flow {
        try {
            val response = apiService.login(email, password)
            emit(Result.success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)


    suspend fun register(name: String, email: String, password: String): RegisterResponse = try {
        apiService.register(name, email, password)
    } catch (e: Exception) {
        throw e
    }

    suspend fun getStoryAll(token: String): AllStoryResponse {

        return apiService.getStoryAll("bearer $token",size=30)
    }

    @ExperimentalPagingApi
    fun getStoryPagingData(token: String): LiveData<PagingData<storyItem>> {
        val pager = Pager(
            config = PagingConfig(pageSize = 5),
            remoteMediator = StoryPagingSource(databaseStory, apiService, token),
            pagingSourceFactory={databaseStory.getListStoryDao().getALlStories()}
        )
        return pager.liveData
    }


    suspend fun tambahStory(
        file: MultipartBody.Part, description: RequestBody
    ): AddStoryResponse {
        val token = getToken().firstOrNull()
        return apiService.addStory("bearer $token", file, description)
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            userPreference: UserPreference, apiService: ApiService,databaseStory: databaseStory
        ): UserRepository = instance ?: synchronized(this) {
            instance ?: UserRepository(userPreference, apiService, databaseStory)
        }.also { instance = it }
    }
}