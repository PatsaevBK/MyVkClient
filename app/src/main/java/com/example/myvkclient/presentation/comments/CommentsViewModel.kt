package com.example.myvkclient.presentation.comments

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.myvkclient.data.repository.RepositoryImpl
import com.example.myvkclient.domain.FeedPost
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CommentsViewModel(
    post: FeedPost,
    application: Application
) : AndroidViewModel(application) {

    private val _screenState: MutableStateFlow<CommentsScreenState> =
        MutableStateFlow(CommentsScreenState.Initial)
    val screenState = _screenState.asStateFlow()

    private val repositoryImpl = RepositoryImpl(application)

    init {
        loadComments(post)
    }

    private fun loadComments(post: FeedPost) {
        viewModelScope.launch {
            val comments = repositoryImpl.loadCommentsToPost(post)
            _screenState.value = CommentsScreenState.Comments(post, comments)
        }
    }

    class CommentsViewModelFactory(
        private val feedPost: FeedPost,
    ): ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
            val application =
                checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
            return CommentsViewModel(feedPost, application) as T
        }
    }
}