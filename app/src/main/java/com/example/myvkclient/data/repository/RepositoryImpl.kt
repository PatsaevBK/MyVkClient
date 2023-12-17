package com.example.myvkclient.data.repository

import android.app.Application
import android.util.Log
import com.example.myvkclient.data.mapper.NewsFeedMapper
import com.example.myvkclient.data.network.ApiFactory
import com.example.myvkclient.domain.FeedPost
import com.example.myvkclient.domain.Repository
import com.example.myvkclient.domain.StatisticItem
import com.example.myvkclient.domain.StatisticType
import com.vk.api.sdk.VKPreferencesKeyValueStorage
import com.vk.api.sdk.auth.VKAccessToken

class RepositoryImpl(
    application: Application,
) : Repository {

    private val storage = VKPreferencesKeyValueStorage(application)

    private val token = VKAccessToken.restore(storage)

    private val apiService = ApiFactory.apiService
    private val mapper: NewsFeedMapper = NewsFeedMapper()

    private val _feedPosts = mutableListOf<FeedPost>()
    val feedPosts: List<FeedPost>
        get() = _feedPosts.toList()

    private var nextFrom: String? = null

    override suspend fun loadNewsFeed(): List<FeedPost> {
        Log.d("RepositoryImpl", "${token?.accessToken}")
        val startFrom = nextFrom

        if (startFrom == null && feedPosts.isNotEmpty()) return feedPosts

        val response = if (startFrom == null) {
            apiService.loadFeedPosts(getAccessToken())
        } else {
            apiService.loadFeedPosts(
                getAccessToken(),
                startFrom
            )
        }
        nextFrom = response.newsFeedContentDto.nextFrom
        val listFeedPost = mapper.mapResponseToPosts(response)
        _feedPosts.addAll(listFeedPost)
        return feedPosts
    }

    override suspend fun ignoreItem(feedPost: FeedPost) {
        val ignoreStatus = apiService.ignoreItem(
            getAccessToken(),
            feedPost.communityId,
            feedPost.id
        )
        Log.d("RepositoryImpl", ignoreStatus.ignoreStatus.status.toString())
        if (ignoreStatus.ignoreStatus.status) {
            _feedPosts.remove(feedPost)
        }
    }

    private fun getAccessToken(): String {
        return token?.accessToken ?: throw IllegalStateException("Token is null")
    }

    override suspend fun changeLikeStatus(feedPost: FeedPost) {
        val response = if (feedPost.isFavorite) {
            apiService.deleteLikeFeedPost(
                token = getAccessToken(),
                ownerId = feedPost.communityId,
                id = feedPost.id
            )
        } else {
            apiService.addLikeFeedPost(
                token = getAccessToken(),
                ownerId = feedPost.communityId,
                id = feedPost.id
            )
        }
        val newLikesCount = response.response.count
        val newStatistics = feedPost.statistics.toMutableList().apply {
            removeIf { it.type == StatisticType.LIKES }
            add(StatisticItem(StatisticType.LIKES, newLikesCount))
        }
        val newPost = feedPost.copy(statistics = newStatistics, isFavorite = !feedPost.isFavorite)
        val postIndex = _feedPosts.indexOf(feedPost)
        _feedPosts[postIndex] = newPost
    }
}