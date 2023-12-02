package com.example.myvkclient.presentation

import com.example.myvkclient.domain.FeedPost

sealed class NewsFeedScreenState {

    data object Initial: NewsFeedScreenState()
    data class Posts(val posts: List<FeedPost>) : NewsFeedScreenState()

}
