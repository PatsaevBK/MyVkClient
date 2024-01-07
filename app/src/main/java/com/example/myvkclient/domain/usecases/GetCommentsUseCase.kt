package com.example.myvkclient.domain.usecases

import com.example.myvkclient.domain.entity.FeedPost
import com.example.myvkclient.domain.entity.PostComment
import com.example.myvkclient.domain.repository.Repository
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject

class GetCommentsUseCase @Inject constructor(
    private val repository: Repository
) {

    operator fun invoke(feedPost: FeedPost): SharedFlow<List<PostComment>> {
        return repository.getComments(feedPost)
    }
}