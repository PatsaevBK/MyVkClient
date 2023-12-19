package com.example.myvkclient.data.network

import com.example.myvkclient.data.network.models.comments.CommentsResponseDto
import com.example.myvkclient.data.network.models.newsFeed.ResponseIgnoreDto
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
    ): ResponseIgnoreDto

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

    @GET("wall.getComments?need_likes=1&extended=1&fields=photo_100&v=5.199")
    suspend fun getCommentsToPost(
        @Query(QUERY_PARAM_ACCESS_TOKEN) token: String,
        @Query(QUERY_PARAM_OWNER_ID) ownerId: Long,
        @Query(QUERY_PARAM_COMMENT_POST_ID) id: Long
    ): CommentsResponseDto

    companion object {
        private const val QUERY_PARAM_ACCESS_TOKEN = "access_token"
        private const val QUERY_PARAM_FEED_POST_ID = "item_id"
        private const val QUERY_PARAM_COMMENT_POST_ID = "post_id"
        private const val QUERY_PARAM_OWNER_ID = "owner_id"
        private const val QUERY_PARAM_START_FROM = "start_from"
    }
}