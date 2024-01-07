package com.example.myvkclient.domain.usecases

import com.example.myvkclient.domain.entity.AuthState
import com.example.myvkclient.domain.repository.Repository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class GetAuthStateFlowUseCase @Inject constructor(
    private val repository: Repository
) {

    operator fun invoke(): StateFlow<AuthState> {
        return repository.getAuthState()
    }
}