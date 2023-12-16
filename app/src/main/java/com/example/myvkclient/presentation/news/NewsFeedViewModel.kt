package com.example.myvkclient.presentation.news

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.myvkclient.data.mapper.NewsFeedMapper
import com.example.myvkclient.data.network.ApiFactory
import com.example.myvkclient.domain.FeedPost
import com.example.myvkclient.domain.StatisticItem
import com.vk.api.sdk.VKPreferencesKeyValueStorage
import com.vk.api.sdk.auth.VKAccessToken
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

    private val mapper = NewsFeedMapper()

    init {
        getFeedPosts()
    }

    private fun getFeedPosts() {
        viewModelScope.launch {
            val storage = VKPreferencesKeyValueStorage(application)
            val token = VKAccessToken.restore(storage) ?: return@launch
            val newsFeedResponseDto = ApiFactory.apiService.loadFeedPosts(token.accessToken)
            val feedPosts = mapper.mapResponseToPosts(newsFeedResponseDto)
//            Log.d("NewsFeedViewModel", token.accessToken)
            Log.d("NewsFeedViewModel", "${feedPosts.map { it.isFavorite }}")
            _screenState.value = NewsFeedScreenState.Posts(feedPosts)
        }
    }

    fun updateCount(feedPost: FeedPost, statisticItem: StatisticItem) {
        val currentState = _screenState.value
        if (currentState !is NewsFeedScreenState.Posts) return

        val oldPosts = currentState.posts.toMutableList()
        val oldStatistics = feedPost.statistics
        val newStatistics = oldStatistics.toMutableList().apply {
            replaceAll { oldItem: StatisticItem ->
                if (oldItem.type == statisticItem.type) {
                    oldItem.copy(count = oldItem.count + 1)
                } else {
                    oldItem
                }
            }
        }.toList()
        val newFeedPost = feedPost.copy(statistics = newStatistics)
        val newPosts = oldPosts.apply {
            replaceAll { oldFeedPost: FeedPost ->
                if (oldFeedPost.id == newFeedPost.id) {
                    newFeedPost
                } else oldFeedPost
            }
        }.toList()
        val newNewsFeedScreenState = NewsFeedScreenState.Posts(newPosts)
        _screenState.value = newNewsFeedScreenState
    }

    fun remove(feedPost: FeedPost) {
        val currentState = _screenState.value
        if (currentState !is NewsFeedScreenState.Posts) return

        val currentList = currentState.posts.toMutableList()
        currentList.remove(feedPost)
        _screenState.value = NewsFeedScreenState.Posts(currentList)
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