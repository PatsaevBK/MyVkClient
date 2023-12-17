package com.example.myvkclient.data.network

import com.example.myvkclient.data.network.models.newsFeed.ResponseIgnore
import com.example.myvkclient.data.network.models.newsFeed.NewsFeedResponseDto
import com.example.myvkclient.data.network.models.newsFeed.LikesCountResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("newsfeed.get?v=5.199")
    suspend fun loadFeedPosts(
        @Query(QUERY_PARAM_ACCESS_TOKEN) token: String
    ): NewsFeedResponseDto

    @GET("newsfeed.get?v=5.199")
    suspend fun loadFeedPosts(
        @Query(QUERY_PARAM_ACCESS_TOKEN) token: String,
        @Query(QUERY_PARAM_START_FROM) startFrom: String
    ): NewsFeedResponseDto

    @GET("newsfeed.ignoreItem?type=wall&v=5.199")
    suspend fun ignoreItem(
        @Query(QUERY_PARAM_ACCESS_TOKEN) token: String,
        @Query(QUERY_PARAM_OWNER_ID) ownerId: Long,
        @Query(QUERY_PARAM_FEED_POST_ID) itemId: Long
    ): ResponseIgnore

    @GET("likes.add?type=post&v=5.199")
    suspend fun addLikeFeedPost(
        @Query(QUERY_PARAM_ACCESS_TOKEN) token: String,
        @Query(QUERY_PARAM_OWNER_ID) ownerId: Long,
        @Query(QUERY_PARAM_FEED_POST_ID) id: Long
    ): LikesCountResponseDto

    @GET("likes.delete?type=post&v=5.199")
    suspend fun deleteLikeFeedPost(
        @Query(QUERY_PARAM_ACCESS_TOKEN) token: String,
        @Query(QUERY_PARAM_OWNER_ID) ownerId: Long,
        @Query(QUERY_PARAM_FEED_POST_ID) id: Long
    ): LikesCountResponseDto

    companion object {
        private const val QUERY_PARAM_ACCESS_TOKEN = "access_token"
        private const val QUERY_PARAM_FEED_POST_ID = "item_id"
        private const val QUERY_PARAM_OWNER_ID = "owner_id"
        private const val QUERY_PARAM_START_FROM = "start_from"
    }
}