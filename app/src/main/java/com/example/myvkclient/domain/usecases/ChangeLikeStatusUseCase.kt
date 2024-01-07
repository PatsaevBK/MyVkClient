package com.example.myvkclient.domain.usecases

import com.example.myvkclient.domain.entity.FeedPost
import com.example.myvkclient.domain.repository.Repository
import javax.inject.Inject

class ChangeLikeStatusUseCase @Inject constructor(
    private val repository: Repository
) {

    suspend operator fun invoke(feedPost: FeedPost) {
        repository.changeLikeStatus(feedPost)
    }
}