package com.example.myvkclient.presentation.news

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.myvkclient.data.repository.RepositoryImpl
import com.example.myvkclient.domain.FeedPost
import com.example.myvkclient.domain.StatisticItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NewsFeedViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val application: Application
) : AndroidViewModel(application) {


    private val initialState = NewsFeedScreenState.Initial

    private val _screenState =
        MutableStateFlow<NewsFeedScreenState>(initialState)
    val screenState = _screenState.asStateFlow()

    private val repository = RepositoryImpl(application)

    init {
        _screenState.value = NewsFeedScreenState.Loading
        loadFeedPosts()
    }

    fun changeLikeStatus(feedPost: FeedPost) {
        viewModelScope.launch {
            repository.changeLikeStatus(feedPost)
            _screenState.value = NewsFeedScreenState.Posts(repository.feedPosts)
        }
    }

    private fun loadFeedPosts() {
        viewModelScope.launch {
            _screenState.value = NewsFeedScreenState.Posts(repository.loadNewsFeed())
        }
    }

    fun loadNextFeedPosts() {
        _screenState.value = NewsFeedScreenState.Posts(
            posts = repository.feedPosts,
            nextDataIsLoading = true
        )
        loadFeedPosts()
    }

    fun ignoreItem(feedPost: FeedPost) {
        viewModelScope.launch {
            repository.ignoreItem(feedPost)
            _screenState.value = NewsFeedScreenState.Posts(repository.feedPosts)
        }

    }


    companion object {
        val Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val application =
                    checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                val savedStateHandle = extras.createSavedStateHandle()
                return NewsFeedViewModel(savedStateHandle, application) as T
            }
        }
    }
}