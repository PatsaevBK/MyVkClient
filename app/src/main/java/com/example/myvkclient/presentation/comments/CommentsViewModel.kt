package com.example.myvkclient.presentation.comments

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.myvkclient.data.repository.RepositoryImpl
import com.example.myvkclient.domain.entity.FeedPost
import com.example.myvkclient.domain.entity.PostComment
import com.example.myvkclient.domain.usecases.GetCommentFromLastCommentsUseCase
import com.example.myvkclient.domain.usecases.GetCommentsUseCase
import com.example.myvkclient.extensions.mergeWith
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class CommentsViewModel(
    private val post: FeedPost,
    application: Application
) : AndroidViewModel(application) {


    private val repositoryImpl = RepositoryImpl(application)
    private val getCommentsUseCase = GetCommentsUseCase(repositoryImpl)
    private val getCommentsFromLastCommentsUseCase = GetCommentFromLastCommentsUseCase(repositoryImpl)
    private val coroutineExceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        Log.d("CommentsViewModel", "ExceptionHandler caught exception: $throwable")
    }

    private var previousList: List<PostComment>? = null

    private val comments = getCommentsUseCase(post)
    private val loadNextCommentEvent = MutableSharedFlow<Unit>()
    private val loadNextCommentFlow = flow<CommentsScreenState> {
        loadNextCommentEvent.collect {
            emit(
                CommentsScreenState.Comments(
                    feedPost = post,
                    comments = comments.replayCache[0],
                    nextCommentIsLoading = true,
                )
            )
        }
    }

    val screenState = comments
        .onEach { Log.d("CommentsViewModel", it.toString()) }
        .filter { it.isNotEmpty() }
        .map {
            CommentsScreenState.Comments(
                feedPost = post,
                comments = it,
                nextCommentIsLoading = false,
                thatIsAll = previousList?.size == it.size
            ) as CommentsScreenState
        }
        .onStart { CommentsScreenState.Loading }
        .mergeWith(loadNextCommentFlow)



    fun loadNextComments(post: FeedPost) {
        viewModelScope.launch(coroutineExceptionHandler) {
            previousList = comments.replayCache[0]
            loadNextCommentEvent.emit(Unit)
            delay(3000)
            getCommentsFromLastCommentsUseCase(post)
        }
    }

    class CommentsViewModelFactory(
        private val feedPost: FeedPost,
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
            val application =
                checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
            return CommentsViewModel(feedPost, application) as T
        }
    }
}