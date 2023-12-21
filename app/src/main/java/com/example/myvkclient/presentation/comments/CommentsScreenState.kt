package com.example.myvkclient.presentation.comments

import com.example.myvkclient.domain.FeedPost
import com.example.myvkclient.domain.PostComment

sealed class CommentsScreenState {
    data object Initial: CommentsScreenState()
    data object Loading: CommentsScreenState()
    data class Comments(
        val feedPost: FeedPost,
        val comments: List<PostComment>,
        val nextCommentIsLoading: Boolean,
        val thatIsAll: Boolean = false
    ) : CommentsScreenState()

}
