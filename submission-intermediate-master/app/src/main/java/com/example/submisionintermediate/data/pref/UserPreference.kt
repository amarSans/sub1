package com.example.submisionintermediate.data.pref

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class UserPreference private constructor(private val dataStore: DataStore<Preferences>) {



    suspend fun simpanPosisi(kode:String){
        dataStore.edit { prefe->prefe[Token]=kode }
    }
    fun getUser():Flow<String>{
        return dataStore.data.map { pref->pref[Token]?:"" }
    }


    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }



    companion object {
        private val Token= stringPreferencesKey("data_token")
        fun getInstance(dataStore: DataStore<Preferences>):UserPreference=UserPreference(dataStore)
    }
}