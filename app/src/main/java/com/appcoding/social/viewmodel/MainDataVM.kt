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
import com.appcoding.social.models.UserStory
import com.appcoding.social.screen.components.pageSizeHome
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
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

    private val _lastSeenId = MutableStateFlow<Long>(-1L)
    val lastSeenId : StateFlow<Long> = _lastSeenId

    private val _userid = MutableStateFlow(-1L)
    val userid :StateFlow<Long> = _userid

    private val _stories = MutableStateFlow<List<StoryResponse>>(emptyList())
    val stories : StateFlow<List<StoryResponse>> = _stories

    private val _userStory = MutableStateFlow<List<UserStory>>(emptyList())
    val userStory : StateFlow<List<UserStory>> = _userStory

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


    //comments
    private val _comments = MutableStateFlow<List<CommentResponse>>(emptyList())
    val comments : StateFlow<List<CommentResponse>> = _comments

    private val _newComment = MutableStateFlow("")
    val newComment : StateFlow<String> = _newComment

    private val _profileImage = MutableStateFlow("")
    val profileImage : StateFlow<String> = _profileImage


    fun getFirst(){
        _userid.value = getUserid()
        _lastSeenId.value = -1
        getData()
        getStoryList()
    }

    fun onRefresh(){
        _postState.value = UiState(isRefreshing = true)
        _lastSeenId.value = -1
        getData()
        getStoryList()
    }

    private fun getStoryList(){
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

    fun getUserStory(userid : Long){
        viewModelScope.launch {
            _storyState.value = UiState(isLoading = true)
            try {
                _userStory.value = storyRepository.getStoryByUserid(userid)

                _storyState.value = UiState(success = true)
            }
            catch(ex : Exception){
                _storyState.value = UiState(message = ex.toString())
            }

        }
    }

    fun getData() {
        viewModelScope.launch {
            try {
                Log.d("lastseenid","before get first data = ${_lastSeenId.value}")
                _postState.value = UiState(isLoading = true)

                val postRes = postRepository.getPostsByFollowers(
                    userId = _userid.value,
                    lastSeenId = _lastSeenId.value,
                    size = pageSizeHome
                )
                Log.d("lastseenid", "post response size = ${postRes.size}")

                if(postRes.size > 0) {
                    if (_lastSeenId.value > -1)
                        _posts.value += postRes
                    else
                        _posts.value = postRes

                    _lastSeenId.value = postRes.lastOrNull()!!.id

                    Log.d("lastseenid", "after get first data = ${_lastSeenId.value}")

                    _postState.value = UiState(success = true)
                }

            } catch (e: Exception) {
                //_postState.value = UiState(message = "خطایی رخ داده است")
                _postState.value = UiState(message = e.toString())
                Log.d("before if", "_post state : ${_postState.value.success}")
            }
        }

    }

    fun getDataMore(last_seen_id : Long){
        viewModelScope.launch {
            try {
                _postState.value = UiState(isLoadMore = true)

                val postRes = postRepository.getPostsByFollowers(
                    userId = _userid.value,
                    lastSeenId = last_seen_id,
                    size = pageSizeHome
                )

                if(postRes.size > 0) {
                    if (last_seen_id > -1)
                        _posts.value += postRes
                    else
                        _posts.value = postRes

                    _lastSeenId.value = postRes.lastOrNull()!!.id

                    _postState.value = UiState(loadMoreSuccess = true)
                }
                else{
                    _postState.value = UiState(isLoadMore = false)
                }

            } catch (e: Exception) {
                _postState.value = UiState(message = e.toString())
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


    fun likePost(postId: Long) {

        _posts.value = _posts.value.map { post ->
            if (post.id == postId) {
                val newLikeCount =
                    if (post.isLike) post.likeCount - 1
                    else post.likeCount + 1
                post.copy(
                    isLike = !post.isLike,
                    likeCount = newLikeCount
                )
            } else post
        }
        viewModelScope.launch {
            val post = _posts.value.find { it.id == postId } ?: return@launch

            if (post.isLike) {
                val response = likeRepository.likePost(
                    LikeRequest(
                        postId = post.id,
                        userId = _userid.value,
                        date = "",
                        time = ""
                    )
                )
            } else {
                val response = likeRepository.disLikePost(
                    postId = post.id,
                    userId = _userid.value
                )
            }
        }
    }

    fun savePost(postId: Long) {
        _posts.value = _posts.value.map { post ->
            if (post.id == postId) {
                post.copy(
                    isSave = !post.isSave
                )
            } else post
        }
        viewModelScope.launch {
            val post = _posts.value.find { it.id == postId } ?: return@launch

            try {
                if (post.isSave) {
                    val response = savePostRepository.savePost(
                        SavePostRequest(
                            userId = _userid.value,
                            postId = postId,
                            date = "14",
                            time = "14"
                        )
                    )
                } else {
                    val response = savePostRepository.unSavePost(
                        userId = _userid.value,
                        postId = postId
                    )
                }
            } catch (ex: Exception) {
                _saveState.value = UiState(success = false, message = ex.toString())
            }
        }
    }

    fun getComments(postId: Long){

        viewModelScope.launch {
            _profileImage.value = userPreferences.getUserProfileFlow().first() ?: ""

            _commentState.value = UiState(isLoading = true)

            try{
                _comments.value = commentRepository.getComments(postId)
                _commentState.value = UiState(success = true)
            }
            catch (ex : Exception){
                _commentState.value = UiState(message = ex.toString())
            }
        }
    }

    fun onCommentChanged(value: String) { _newComment.value = value}

    fun sendComment(postId: Long){
        _posts.value = _posts.value.map { post ->
            if(post.id == postId){
                val newCommentCount = post.commentCount +1
                post.copy(commentCount = newCommentCount)
            }
            else post
        }
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

}