package com.example.submisionintermediate.di

import android.content.Context
import com.example.submisionintermediate.data.UserRepository
import com.example.submisionintermediate.data.database.databaseStory
import com.example.submisionintermediate.data.pref.UserPreference
import com.example.submisionintermediate.data.pref.dataStore
import com.example.submisionintermediate.data.retrofit.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService=ApiConfig.getApiService()
        val databaseStory = databaseStory.getDatabase(context)
        return UserRepository.getInstance(pref,apiService, databaseStory)
    }
}