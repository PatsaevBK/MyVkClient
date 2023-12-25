package com.example.myvkclient.presentation.news

import com.example.myvkclient.domain.entity.FeedPost

sealed class NewsFeedScreenState {

    data object Initial : NewsFeedScreenState()

    data object Loading: NewsFeedScreenState()

    data object Error: NewsFeedScreenState()

    data class Posts(
        val posts: List<FeedPost>,
        val nextDataIsLoading: Boolean = false
    ) : NewsFeedScreenState()

}
