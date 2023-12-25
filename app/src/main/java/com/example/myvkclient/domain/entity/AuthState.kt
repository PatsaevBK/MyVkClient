package com.example.myvkclient.domain.entity

sealed class AuthState {

    data object Initial: AuthState()
    data object Authorized: AuthState()

    data object NotAuthorized: AuthState()
}
