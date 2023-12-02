package com.example.myvkclient.presentation

import com.example.myvkclient.domain.FeedPost
import com.example.myvkclient.domain.PostComment

sealed class CommentsScreenState {
    data object Initial: CommentsScreenState()
    data class Comments(
        val feedPost: FeedPost,
        val comments: List<PostComment>
    ) : CommentsScreenState()

}
