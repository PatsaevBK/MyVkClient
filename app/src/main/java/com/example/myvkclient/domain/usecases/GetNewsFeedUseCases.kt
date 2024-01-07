package com.example.myvkclient.domain.usecases

import com.example.myvkclient.domain.entity.NewsFeedResult
import com.example.myvkclient.domain.repository.Repository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class GetNewsFeedUseCases @Inject constructor(
    private val repository: Repository
) {
    operator fun invoke(): StateFlow<NewsFeedResult> {
        return repository.getNewsFeed()
    }
}