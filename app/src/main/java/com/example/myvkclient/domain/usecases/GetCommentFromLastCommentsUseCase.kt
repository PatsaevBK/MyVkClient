package com.example.myvkclient.domain.usecases

import com.example.myvkclient.domain.entity.FeedPost
import com.example.myvkclient.domain.entity.PostComment
import com.example.myvkclient.domain.repository.Repository
import kotlinx.coroutines.flow.Flow

class GetCommentFromLastCommentsUseCase(
    private val repository: Repository
) {

    suspend operator fun invoke(feedPost: FeedPost) {
        repository.getCommentsFromLastComment(feedPost)
    }
}