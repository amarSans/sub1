package com.example.submisionintermediate.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.submisionintermediate.data.UserRepository
import com.example.submisionintermediate.data.database.storyItem
import com.example.submisionintermediate.data.response.ListStoryItem
import kotlinx.coroutines.launch

class MainViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _userSession = MutableLiveData<String?>()
    val userSession: LiveData<String?>
        get() = _userSession


    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error


    init {
        getSession()
    }


    fun getSession() {
        viewModelScope.launch {
            userRepository.getToken().collect { session ->
                _userSession.value = session
            }
        }
    }


    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
        }
    }


    private val _storyPagingData = MutableLiveData<PagingData<ListStoryItem>>()
    val storyPagingData: LiveData<PagingData<ListStoryItem>> get() = _storyPagingData

    @ExperimentalPagingApi
    fun getPagingStories(token: String): LiveData<PagingData<storyItem>> {
        return userRepository.getStoryPagingData(token).cachedIn(viewModelScope)
    }

}





