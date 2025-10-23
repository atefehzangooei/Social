package com.appcoding.social.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appcoding.social.UserPreferences
import com.appcoding.social.data.repository.PostRepository
import com.appcoding.social.models.PostResponse
import com.appcoding.social.models.StoryResponse
import com.appcoding.social.models.pageSizeHome
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainDataVM @Inject constructor(
    private val userPreferences: UserPreferences,
    private val postRepository: PostRepository
) : ViewModel() {

    private val _posts = MutableStateFlow<List<PostResponse>>(emptyList())
    val posts : StateFlow<List<PostResponse>> = _posts

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing : StateFlow<Boolean> = _isRefreshing

    private val _isLoading = MutableStateFlow(false)
    val isLoading : StateFlow<Boolean> = _isLoading

    private val _lastSeenId = MutableStateFlow<Long?>(-1L)

    private val _message = MutableStateFlow("")
    //val message : StateFlow<String> = _message

    private val _userid = MutableStateFlow(-1L)
    val userid :StateFlow<Long> = _userid

    private val _stories = MutableStateFlow<List<StoryResponse>>(emptyList())
    val stories : StateFlow<List<StoryResponse>> = _stories

    private val _isLoadingStory = MutableStateFlow(false)
    val isLoadingStory : StateFlow<Boolean> = _isLoadingStory


    fun getFirst(){
        _userid.value = getUserid()
        getData()
        getStory()
    }

    fun onRefresh(){
        _isRefreshing.value = true
        _lastSeenId.value = -1
        getData()
        getStory()
    }

    private fun getStory(){
        viewModelScope.launch {
            _isLoadingStory.value = true
            try{
                val storyRes = postRepository.getStoryOfFollowers(_userid.value)
                _stories.value = storyRes
            }
            catch(ex : Exception){
               _isLoadingStory.value = false
            }
            finally {
                _isLoadingStory.value = false
            }
        }
    }

    fun getData() {
        viewModelScope.launch {
            try {
                _isLoading.value = true

                val postRes = postRepository.getPostsByFollowers(
                    userId = _userid.value,
                    lastSeenId = _lastSeenId.value,
                    size = pageSizeHome
                )
                if(_lastSeenId.value !!> -1)
                     _posts.value += postRes
                else
                    _posts.value = postRes

                _lastSeenId.value = postRes.lastOrNull()?.id
            } catch (e: Exception) {
                _message.value = "خطایی رخ داده است"
            } finally {
                _isLoading.value = false
                _isRefreshing.value = false
            }
        }

    }

    private fun getUserid() : Long {
        viewModelScope.launch {
            userPreferences.getUserIdFlow().collect { id ->
                _userid.value = id ?: 0L
            }
        }
        return _userid.value
    }

}