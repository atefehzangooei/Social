package com.appcoding.social.viewmodel

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appcoding.social.data.api.LikeApi
import com.appcoding.social.data.repository.CommentRepository
import com.appcoding.social.data.repository.LikeRepository
import com.appcoding.social.screen.components.UserPreferences
import com.appcoding.social.data.repository.PostRepository
import com.appcoding.social.data.repository.SavePostRepository
import com.appcoding.social.data.repository.StoryRepository
import com.appcoding.social.models.CommentRequest
import com.appcoding.social.models.CommentResponse
import com.appcoding.social.models.LikeRequest
import com.appcoding.social.models.PostResponse
import com.appcoding.social.models.SavePostRequest
import com.appcoding.social.models.StoryResponse
import com.appcoding.social.screen.components.pageSizeHome
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainDataVM @Inject constructor(
    private val userPreferences: UserPreferences,
    private val postRepository: PostRepository,
    private val storyRepository: StoryRepository,
    private val likeRepository: LikeRepository,
    private val savePostRepository: SavePostRepository,
    private val commentRepository: CommentRepository
) : ViewModel() {


    private val _posts = MutableStateFlow<List<PostResponse>>(emptyList())
    val posts : StateFlow<List<PostResponse>> = _posts

    private val _lastSeenId = MutableStateFlow<Long?>(-1L)

    private val _userid = MutableStateFlow(-1L)
    val userid :StateFlow<Long> = _userid

    private val _stories = MutableStateFlow<List<StoryResponse>>(emptyList())
    val stories : StateFlow<List<StoryResponse>> = _stories

    //States
    private val _postState = MutableStateFlow(UiState())
    val postState : StateFlow<UiState> = _postState

    private val _storyState = MutableStateFlow(UiState())
    val storyState : StateFlow<UiState> = _storyState

    private val _likeState = MutableStateFlow(UiState())
    val likeState : StateFlow<UiState> = _likeState

    private val _commentState = MutableStateFlow(UiState())
    val commentState : StateFlow<UiState> = _commentState

    private val _saveState = MutableStateFlow(UiState())
    val saveState : StateFlow<UiState> = _saveState

    //post
    private val _isSaved = MutableStateFlow(false)
    val isSaved : StateFlow<Boolean> = _isSaved

    private val _isLiked = MutableStateFlow(false)
    val isLiked : StateFlow<Boolean> = _isLiked

    private val _commentCount = MutableStateFlow(0)
    val commentCount : StateFlow<Int> = _commentCount

    private val _likeCount = MutableStateFlow(0)
    val likeCount : StateFlow<Int> = _likeCount

    //comments
    private val _comments = MutableStateFlow<List<CommentResponse>>(emptyList())
    val comments : StateFlow<List<CommentResponse>> = _comments

    private val _newComment = MutableStateFlow("")
    val newComment : StateFlow<String> = _newComment


    fun getFirst(){
        _userid.value = getUserid()
        getData()
        getStory()
    }

    fun onRefresh(){
        _postState.value = UiState(isRefreshing = true)
        _lastSeenId.value = -1
        getData()
        getStory()
    }

    private fun getStory(){
        viewModelScope.launch {
            _storyState.value = UiState(isLoading = true)
            try{
                val storyRes = storyRepository.getStoryOfFollowers(_userid.value)
                _stories.value = storyRes

                _storyState.value = UiState(success = true)

            }
            catch(ex : Exception){
               _storyState.value = UiState(isLoading = false)
                Log.d("before if", "_story state : ${_storyState.value.success}")
            }

        }
    }

    fun getData() {
        viewModelScope.launch {
            try {
                _postState.value = UiState(isLoading = true)

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

                _postState.value = UiState(success = true)


            } catch (e: Exception) {
                _postState.value = UiState(message = "خطایی رخ داده است")
                Log.d("before if", "_post state : ${_postState.value.success}")
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

    fun likePost(postId : Long) {
        viewModelScope.launch {

            if (_isLiked.value) {
                _likeCount.value++
                val response = likeRepository.likePost(
                    LikeRequest(
                        postId = postId,
                        userId = _userid.value,
                        date = "",
                        time = ""
                    )
                )
                if(response.success){
                    _isLiked.value = !_isLiked.value
                }
                else{
                    _likeState.value = UiState(success = false, message = response.message)
                }
            } else {
                _likeCount.value--
                val response = likeRepository.disLikePost(
                    postId = postId,
                    userId = _userid.value
                )
                if(response.success){
                    _isLiked.value = !_isLiked.value
                }
                else{
                    _likeState.value = UiState(success = false, message = response.message)
                }
            }
        }
    }

    fun savePost(postId : Long){
        viewModelScope.launch {
                if(_isSaved.value){
                    try {
                        val response = savePostRepository.savePost(
                            SavePostRequest(
                                userId = _userid.value,
                                postId = postId,
                                date = "14",
                                time = "14"
                            )
                        )
                        if(response.success) {
                            _isSaved.value = !_isSaved.value
                            _saveState.value = UiState(success = true, message = "با موفقیت ذخیره شد")
                        }
                        else{
                            _saveState.value = UiState(success = false, message = response.message)
                        }
                    }
                    catch (ex : Exception){
                        _saveState.value = UiState(success = false, message = ex.toString())
                    }
                }
                else{
                    try {
                        val response = savePostRepository.unSavePost(
                            userId = _userid.value,
                            postId = postId
                        )
                        if(response.success) {
                            _isSaved.value = !_isSaved.value
                            _saveState.value = UiState(success = true, message = "unsaved")
                        }
                        else{
                            _saveState.value = UiState(success = false, message = response.message)
                        }
                    }
                    catch (ex : Exception){
                        _saveState.value = UiState(success = false, message = ex.toString())
                    }
                }
        }
    }

    fun getComments(postId: Long){
        viewModelScope.launch {
            _commentState.value = UiState(isLoading = true)

            try{
                _comments.value = commentRepository.getComments(postId)
                _commentState.value = UiState(success = true)
               // post.commentCount++
            }
            catch (ex : Exception){
                _commentState.value = UiState(message = ex.toString())
            }
        }
    }

    fun onCommentChanged(value: String) { _newComment.value = value}

    fun sendComment(postId : Long){
        viewModelScope.launch {
            _commentState.value = UiState(isLoading = true)
            try {
                val commentRequest = CommentRequest(
                    postId = postId,
                    userId = _userid.value,
                    comment = _newComment.value,
                    date = "1404",
                    time = "18:00"
                )
                val addedComment = commentRepository.addComment(commentRequest)
                _comments.value +=  addedComment
                _newComment.value = ""

                _commentState.value = UiState(success = true)

            }
            catch (ex : Exception){
                _commentState.value = UiState(message = ex.toString())
            }
        }
    }


    fun setPost(post : PostResponse){
        _isLiked.value = post.isLike
        _isSaved.value = post.isSave
        _likeCount.value = post.likeCount
        _commentCount.value = post.commentCount
    }
}