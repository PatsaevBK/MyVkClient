package com.example.myvkclient.domain

interface Repository {

    suspend fun loadNewsFeed(): List<FeedPost>

    suspend fun changeLikeStatus(feedPost: FeedPost)

    suspend fun ignoreItem(feedPost: FeedPost)

    suspend fun loadCommentsToPost(feedPost: FeedPost): List<PostComment>
}