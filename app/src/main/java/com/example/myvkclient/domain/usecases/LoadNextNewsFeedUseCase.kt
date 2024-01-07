package com.example.myvkclient.domain.usecases

import com.example.myvkclient.domain.repository.Repository
import javax.inject.Inject

class LoadNextNewsFeedUseCase @Inject constructor(
    private val repository: Repository
) {

    suspend operator fun invoke() {
        repository.loadNextNewsFeed()
    }
}