package com.example.myvkclient.data.network

import com.example.myvkclient.data.network.models.NewsFeedResponseDto
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @GET("newsfeed.get?v=5.199")
    suspend fun loadFeedPosts(
        @Query(QUERY_PARAM_ACCESS_TOKEN) token: String
    ): NewsFeedResponseDto

    @POST("likes.add?type=post?v=5.199")
    suspend fun likeFeedPost(
        @Query(QUERY_PARAM_ACCESS_TOKEN) token: String,
        @Query(QUERY_PARAM_FEED_POST_ID) id: Int
    )

    companion object {
        private const val QUERY_PARAM_ACCESS_TOKEN = "access_token"
        private const val QUERY_PARAM_FEED_POST_ID = "item_id"
    }
}