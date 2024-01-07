package com.example.myvkclient.presentation.news

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myvkclient.domain.entity.FeedPost
import com.example.myvkclient.domain.entity.NewsFeedResult
import com.example.myvkclient.domain.usecases.ChangeLikeStatusUseCase
import com.example.myvkclient.domain.usecases.GetNewsFeedUseCases
import com.example.myvkclient.domain.usecases.IgnorePostUseCase
import com.example.myvkclient.domain.usecases.LoadNextNewsFeedUseCase
import com.example.myvkclient.extensions.mergeWith
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

class NewsFeedViewModel @Inject constructor(
    private val getNewsFeedUseCases: GetNewsFeedUseCases,
    private val loadNextNewsFeedUseCase:LoadNextNewsFeedUseCase,
    private val ignorePostUseCase: IgnorePostUseCase,
    private val changeLikeStatusUseCase:ChangeLikeStatusUseCase
) : ViewModel() {

    private val newsFeedFlow = getNewsFeedUseCases()

    private val coroutineExceptionHandler =
        CoroutineExceptionHandler { coroutineContext, throwable ->
            Log.d("NewsFeedViewModel", "Exception is caught by exceptionHandler")
        }

    private val loadNextDataEvents = MutableSharedFlow<Unit>()
    private val loadNextDataFlow = flow<NewsFeedScreenState> {
        loadNextDataEvents.collect {
            emit(
                NewsFeedScreenState.Posts(
                    (newsFeedFlow.value as NewsFeedResult.Success).listOfPosts,
                    true
                )
            )
        }
    }

    val screenState = newsFeedFlow
        .map { it as NewsFeedResult.Success }
        .filter { it.listOfPosts.isNotEmpty() }
        .map { NewsFeedScreenState.Posts(it.listOfPosts) as NewsFeedScreenState }
        .onStart { emit(NewsFeedScreenState.Loading) }
        .mergeWith(loadNextDataFlow)
        .catch { emit(NewsFeedScreenState.Error) }


    fun changeLikeStatus(feedPost: FeedPost) {
        viewModelScope.launch(coroutineExceptionHandler) {
            changeLikeStatusUseCase(feedPost)
        }
    }


    fun loadNextFeedPosts() {
        viewModelScope.launch {
            loadNextDataEvents.emit(Unit)
            loadNextNewsFeedUseCase()
        }
    }

    fun ignoreItem(feedPost: FeedPost) {
        viewModelScope.launch(coroutineExceptionHandler) {
            ignorePostUseCase(feedPost)
        }

    }

}