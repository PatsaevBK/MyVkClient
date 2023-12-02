package com.example.myvkclient.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myvkclient.domain.FeedPost
import com.example.myvkclient.domain.PostComment
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CommentsViewModel(
    post: FeedPost
) : ViewModel() {

    private val _screenState: MutableStateFlow<CommentsScreenState> =
        MutableStateFlow(CommentsScreenState.Initial)
    val screenState = _screenState.asStateFlow()

    init {
        loadComments(post)
    }

    fun loadComments(post: FeedPost) {
        val comments = List(10) { PostComment(it, "Name $it") }
        _screenState.value = CommentsScreenState.Comments(post, comments)
    }

    class CommentsViewModelFactory(
        private val feedPost: FeedPost
    ): ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return CommentsViewModel(feedPost) as T
        }
    }
}