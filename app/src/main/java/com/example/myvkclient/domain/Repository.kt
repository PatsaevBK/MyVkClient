package com.example.myvkclient.domain

interface Repository {

    suspend fun loadNextNewsFeed()

    suspend fun changeLikeStatus(feedPost: FeedPost)

    suspend fun ignoreItem(feedPost: FeedPost)

    suspend fun loadCommentsToPost(feedPost: FeedPost)

    suspend fun loadCommentsToPostFromLastComment(feedPost: FeedPost)
}