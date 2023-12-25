package com.example.myvkclient.domain.usecases

import com.example.myvkclient.domain.entity.AuthState
import com.example.myvkclient.domain.repository.Repository
import kotlinx.coroutines.flow.StateFlow

class LoadNextNewsFeedUseCase(
    private val repository: Repository
) {

    suspend operator fun invoke() {
        repository.loadNextNewsFeed()
    }
}