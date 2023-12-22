package com.example.myvkclient.data.repository

import android.app.Application
import android.util.Log
import com.example.myvkclient.data.mapper.NewsFeedMapper
import com.example.myvkclient.data.network.ApiFactory
import com.example.myvkclient.domain.FeedPost
import com.example.myvkclient.domain.PostComment
import com.example.myvkclient.domain.Repository
import com.example.myvkclient.domain.StatisticItem
import com.example.myvkclient.domain.StatisticType
import com.example.myvkclient.extensions.mergeWith
import com.vk.api.sdk.VKPreferencesKeyValueStorage
import com.vk.api.sdk.auth.VKAccessToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn

class RepositoryImpl(
    application: Application,
) : Repository {

    private val storage = VKPreferencesKeyValueStorage(application)

    private val token = VKAccessToken.restore(storage)

    private val apiService = ApiFactory.apiService
    private val mapper: NewsFeedMapper = NewsFeedMapper()

    private val _feedPosts = mutableListOf<FeedPost>()
    private val feedPosts: List<FeedPost>
        get() = _feedPosts.toList()

    private var nextFrom: String? = null

    private val _commentsToFeedPost = mutableListOf<PostComment>()
    private val commentsToFeedPost: List<PostComment>
        get() = _commentsToFeedPost.toList()

    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    private val nextDataNeededEvents = MutableSharedFlow<Unit>(replay = 1)

    private val refreshListFlow = MutableSharedFlow<List<FeedPost>>()

    private val loadedListFlow = flow {
        nextDataNeededEvents.emit(Unit)
        nextDataNeededEvents.collect {
            Log.d("RepositoryImpl", "${token?.accessToken}")
            val startFrom = nextFrom

            if (startFrom == null && feedPosts.isNotEmpty()) {
                emit(feedPosts)
                return@collect
            }
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
            Log.d("RepositoryImpl", "$listFeedPost")
            emit(feedPosts)
        }
    }

    val newsFeed = loadedListFlow
        .mergeWith(refreshListFlow)
        .stateIn(
            coroutineScope,
            SharingStarted.Lazily,
            feedPosts
        )

    private val nextCommentsNeeded = MutableSharedFlow<Unit>(replay = 1)
    private val refreshPostCommentsFlow = MutableSharedFlow<List<PostComment>>()

    val loadedComments = flow {
        nextCommentsNeeded.emit(Unit)
        nextCommentsNeeded.collect {
            emit(commentsToFeedPost)
        }
    }.mergeWith(refreshPostCommentsFlow)
        .shareIn(
            coroutineScope,
            SharingStarted.Lazily,
            1
        )

    private var flagLastAmountOfComments = false

    override suspend fun loadNextNewsFeed() {
        nextDataNeededEvents.emit(Unit)
    }

    override suspend fun ignoreItem(feedPost: FeedPost) {
        val ignoreStatus = apiService.ignoreItem(
            getAccessToken(),
            feedPost.communityId,
            feedPost.id
        )
        Log.d("RepositoryImpl", ignoreStatus.ignoreStatusDto.status.toString())
        if (ignoreStatus.ignoreStatusDto.status) {
            _feedPosts.remove(feedPost)
        }
        refreshListFlow.emit(feedPosts)
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
        refreshListFlow.emit(feedPosts)
    }

    override suspend fun loadCommentsToPost(feedPost: FeedPost) {
        _commentsToFeedPost.clear()
        flagLastAmountOfComments = false
        val response = apiService.getCommentsToPost(
            token = getAccessToken(),
            ownerId = feedPost.communityId,
            id = feedPost.id
        )
        val listOfComments = mapper.mapCommentsResponseDtoToPostComment(response)
        if (listOfComments.isNotEmpty()) {
            _commentsToFeedPost.addAll(listOfComments)
        }
        refreshPostCommentsFlow.emit(commentsToFeedPost)
    }

    override suspend fun loadCommentsToPostFromLastComment(feedPost: FeedPost) {
//        if (flagLastAmountOfComments) return
        val response = apiService.getCommentsToPost(
            token = getAccessToken(),
            ownerId = feedPost.communityId,
            id = feedPost.id,
            startCommentId = _commentsToFeedPost.last().id
        )
        val nextListOfComments = mapper.mapCommentsResponseDtoToPostComment(response)
        if (nextListOfComments.isNotEmpty()) {
            _commentsToFeedPost.addAll(nextListOfComments)
        }
        refreshPostCommentsFlow.emit(commentsToFeedPost)
//        if (nextListOfComments.size < PostComment.DEFAULT_AMOUNT_OF_DOWNLOAD_COMMENTS) {
//            flagLastAmountOfComments = true
//        }
    }
}