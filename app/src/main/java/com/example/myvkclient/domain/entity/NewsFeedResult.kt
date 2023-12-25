package com.example.myvkclient.domain.entity

sealed class NewsFeedResult {

    data class Success(val listOfPosts: List<FeedPost>): NewsFeedResult()

    data object Error: NewsFeedResult()
}
