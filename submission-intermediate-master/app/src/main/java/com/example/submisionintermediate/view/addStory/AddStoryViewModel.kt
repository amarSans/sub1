package com.example.submisionintermediate.view.addStory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.RemoteMediator
import com.example.submisionintermediate.data.UserRepository
import com.example.submisionintermediate.data.response.AddStoryResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

sealed class StoryResult{
    data class Success(val response: AddStoryResponse) : StoryResult()
    data class Error(val exception: Throwable) : StoryResult()
}
class AddStoryViewModel(private val repository: UserRepository) :ViewModel(){
    private val _tambahStoryResult = MutableStateFlow<StoryResult?>(null)
    val tambahStoryResult: MutableStateFlow<StoryResult?> get() = _tambahStoryResult



    fun tambahStory(file: MultipartBody.Part, description: RequestBody) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = repository.tambahStory( file, description)
                _tambahStoryResult.value = StoryResult.Success(result)
            }catch (e: Exception) {
                e.printStackTrace()
                _tambahStoryResult.value = StoryResult.Error(e)
            }
        }
    }
}