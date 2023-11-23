package com.example.myvkclient

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.myvkclient.domain.FeedPost
import com.example.myvkclient.domain.StatisticItem
import com.example.myvkclient.ui.theme.NavigationItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {

    private val listFeedPost = List(100) { FeedPost(it) }

    private val _bottomStateFlow = MutableStateFlow(NavigationItem.HOME)
    val bottomStateFlow = _bottomStateFlow.asStateFlow()


    private val _listFeedPostStateFlow = MutableStateFlow(listFeedPost)
    val listFeedPostStateFlow = _listFeedPostStateFlow.asStateFlow()

    fun changeBottomState(navigationItem: NavigationItem) {
        _bottomStateFlow.apply {
            value = when (navigationItem) {
                NavigationItem.HOME -> NavigationItem.HOME
                NavigationItem.FAVORITE -> NavigationItem.FAVORITE
                NavigationItem.PROFILE -> NavigationItem.PROFILE
            }
        }
    }

    fun updateCount(feedPost: FeedPost, statisticItem: StatisticItem) {
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
        val newList = _listFeedPostStateFlow.value.toMutableList().apply {
            replaceAll {
                if (it == feedPost) {
                    newFeedPost
                } else it
            }
        }.toList()
        _listFeedPostStateFlow.value = newList
    }

    fun remove(feedPost: FeedPost) {
        val currentList = _listFeedPostStateFlow.value.toMutableList()
        currentList.remove(feedPost)
        _listFeedPostStateFlow.value = currentList.toList()
    }


    companion object {
        val Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val application =
                    checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                val savedStateHandle = extras.createSavedStateHandle()
                return ViewModel(savedStateHandle) as T
            }
        }
    }
}