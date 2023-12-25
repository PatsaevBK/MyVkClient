package com.example.myvkclient.data.repository

import android.app.Application
import android.util.Log
import com.example.myvkclient.data.mapper.NewsFeedMapper
import com.example.myvkclient.data.network.ApiFactory
import com.example.myvkclient.domain.entity.AuthState
import com.example.myvkclient.domain.entity.FeedPost
import com.example.myvkclient.domain.entity.NewsFeedResult
import com.example.myvkclient.domain.entity.PostComment
import com.example.myvkclient.domain.repository.Repository
import com.example.myvkclient.domain.entity.StatisticItem
import com.example.myvkclient.domain.entity.StatisticType
import com.example.myvkclient.extensions.mergeWith
import com.vk.api.sdk.VKPreferencesKeyValueStorage
import com.vk.api.sdk.auth.VKAccessToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn

class RepositoryImpl(
    application: Application,
) : Repository {

    private val storage = VKPreferencesKeyValueStorage(application)

    private val token
        get() = VKAccessToken.restore(storage)

    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    private val checkAuthStateEvent = MutableSharedFlow<Unit>(replay = 1)

    private val authState = flow {
        checkAuthStateEvent.emit(Unit)
        checkAuthStateEvent.collect {
            val currentToken = token
            val loggedIn = currentToken != null && currentToken.isValid
            val authState = if (loggedIn) AuthState.Authorized else AuthState.NotAuthorized
            emit(authState)
        }
    }.stateIn(
        coroutineScope,
        SharingStarted.Lazily,
        AuthState.Initial
    )
    private val apiService = ApiFactory.apiService

    private val mapper: NewsFeedMapper = NewsFeedMapper()
    private val _feedPosts = mutableListOf<FeedPost>()

    private val feedPosts: List<FeedPost>
        get() = _feedPosts.toList()

    private var nextFrom: String? = null
    private val _commentsToFeedPost = mutableListOf<PostComment>()

    private val commentsToFeedPost: List<PostComment>
        get() = _commentsToFeedPost.toList()

    private val nextDataNeededEvents = MutableSharedFlow<Unit>(replay = 1)

    private val refreshListFlow = MutableSharedFlow<NewsFeedResult>()

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
        .map { NewsFeedResult.Success(it) as NewsFeedResult }
        .retry {
            delay(RETRY_TIMEOUT_MILLIS)
            true
        }
        .onEach { Log.d("RepositoryImpl", it.toString()) }

    private val newsFeed = loadedListFlow
        .mergeWith(refreshListFlow)
        .stateIn(
            coroutineScope,
            SharingStarted.Lazily,
            NewsFeedResult.Success(feedPosts)
        )

    private val nextCommentsFlowEvent = MutableSharedFlow<Unit>()
    override fun getAuthState(): StateFlow<AuthState> {
        return authState
    }

    override fun getNewsFeed(): StateFlow<NewsFeedResult> {
        return newsFeed
    }

    override suspend fun checkAuthState() {
        checkAuthStateEvent.emit(Unit)
    }

    private fun getAccessToken(): String {
        return token?.accessToken ?: throw IllegalStateException("Token is null")
    }

    override fun getComments(feedPost: FeedPost) = flow {
        _commentsToFeedPost.clear()
        Log.d("RepositoryImpl", "LoadCommentsToPost")
        val response = apiService.getCommentsToPost(
            token = getAccessToken(),
            ownerId = feedPost.communityId,
            id = feedPost.id
        )
        val listOfComments = mapper.mapCommentsResponseDtoToPostComment(response)
        if (listOfComments.isNotEmpty()) {
            _commentsToFeedPost.addAll(listOfComments)
        }
        emit(commentsToFeedPost)
        nextCommentsFlowEvent.collect {
            emit(commentsToFeedPost)
        }
    }
        .retry {
            delay(RETRY_TIMEOUT_MILLIS)
            Log.d("RepositoryImpl", "retry")
            true
        }
        .shareIn(
            coroutineScope,
            SharingStarted.Lazily,
            1
        )

    override suspend fun getCommentsFromLastComment(feedPost: FeedPost) {
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
        nextCommentsFlowEvent.emit(Unit)
    }

    override suspend fun loadNextNewsFeed() {
        Log.d("RepositoryImpl", "loadNextNewsFeed")
        nextDataNeededEvents.emit(Unit)
    }

    override suspend fun ignorePost(feedPost: FeedPost) {
        val ignoreStatus = apiService.ignoreItem(
            getAccessToken(),
            feedPost.communityId,
            feedPost.id
        )
        Log.d("RepositoryImpl", ignoreStatus.ignoreStatusDto.status.toString())
        if (ignoreStatus.ignoreStatusDto.status) {
            _feedPosts.remove(feedPost)
        }
        refreshListFlow.emit(NewsFeedResult.Success(feedPosts))
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
        refreshListFlow.emit(NewsFeedResult.Success(feedPosts))
    }

    companion object {

        const val RETRY_TIMEOUT_MILLIS = 3000L
        const val COUNT_OF_RETRY = 2L
    }
}