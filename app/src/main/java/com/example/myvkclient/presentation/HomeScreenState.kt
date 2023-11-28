package com.example.myvkclient.presentation

import com.example.myvkclient.domain.FeedPost
import com.example.myvkclient.domain.PostComment

sealed class HomeScreenState {
    data class Posts(val posts: List<FeedPost>) : HomeScreenState()

    data class Comments(val feedPost: FeedPost, val comments: List<PostComment>) : HomeScreenState()
}
