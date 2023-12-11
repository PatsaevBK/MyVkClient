package com.example.myvkclient.presentation.main

sealed class AuthState {

    data object Initial: AuthState()
    data object Authorized: AuthState()

    data object NotAuthorized: AuthState()
}
