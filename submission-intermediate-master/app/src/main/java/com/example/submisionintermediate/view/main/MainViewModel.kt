package com.example.submisionintermediate.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.submisionintermediate.data.UserRepository
import com.example.submisionintermediate.data.response.ListStoryItem
import com.example.submisionintermediate.data.retrofit.ApiConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel constructor(private val userRepository: UserRepository) : ViewModel() {
    private val _userSession = MutableLiveData<String?>()
    val userSession:LiveData<String?> get() = _userSession
    fun getSession(){
        viewModelScope.launch {
            userRepository.getSesion().collect{
                session-> _userSession.value=session
            }
        }
    }
    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
        }
    }
    private val _storyList = MutableLiveData<List<ListStoryItem>?>()
    val storyList: MutableLiveData<List<ListStoryItem>?>
        get() = _storyList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error



    fun getStories() {
        _isLoading.value = true

        viewModelScope.launch(Dispatchers.IO) {
            try {
                userRepository.getSesion().collect { token ->
                    if (!token.isNullOrBlank()) {
                        val response = userRepository.apiService.getStory("Bearer $token")
                        _storyList.postValue(response.listStory as List<ListStoryItem>?)
                        _isLoading.postValue(false)
                    } else {
                        _error.postValue("Token is null or empty.")
                        _isLoading.postValue(false)
                    }
                }
            } catch (e: Exception) {
                _error.postValue("Error fetching stories: ${e.message}")
                _isLoading.postValue(false)
            }
        }
    }

}