package com.example.submisionintermediate.view.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.submisionintermediate.data.UserRepository
import com.example.submisionintermediate.data.database.storyItem
import com.example.submisionintermediate.data.response.ListStoryItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class mapViewModel (private val userRepository: UserRepository) : ViewModel() {

    private val _storyList = MutableLiveData<List<storyItem>?>()
    val storyList: LiveData<List<storyItem>?>
        get() = _storyList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    fun getStoriesWithLocation() {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                userRepository.getToken().firstOrNull()?.let { token ->
                    if (token.isNotBlank()) {
                        val response = userRepository.getStoryAll(token)
                        _storyList.postValue(response.listStory as List<storyItem>?)
                    } else {
                        _error.postValue("Token is null or empty.")
                    }
                }

            } catch (e: Exception) {
                _error.postValue("Error fetching stories: ${e.message}")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }
}