package com.example.myvkclient.domain.repository

import com.example.myvkclient.domain.entity.AuthState
import com.example.myvkclient.domain.entity.FeedPost
import com.example.myvkclient.domain.entity.NewsFeedResult
import com.example.myvkclient.domain.entity.PostComment
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface Repository {

    fun getAuthState(): StateFlow<AuthState>

    suspend fun checkAuthState()

    fun getNewsFeed(): StateFlow<NewsFeedResult>

    fun getComments(feedPost: FeedPost): SharedFlow<List<PostComment>>

    suspend fun getCommentsFromLastComment(feedPost: FeedPost)

    suspend fun loadNextNewsFeed()

    suspend fun ignorePost(feedPost: FeedPost)

    suspend fun changeLikeStatus(feedPost: FeedPost)
}