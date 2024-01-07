package com.example.myvkclient.domain.usecases

import com.example.myvkclient.domain.repository.Repository
import javax.inject.Inject

class CheckAuthStateUseCase @Inject constructor(
    private val repository: Repository
) {

    suspend operator fun invoke() {
        repository.checkAuthState()
    }
}