package com.example.myvkclient.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myvkclient.domain.usecases.CheckAuthStateUseCase
import com.example.myvkclient.domain.usecases.GetAuthStateFlowUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel  @Inject constructor(
    private val getAuthStateFlowUseCase: GetAuthStateFlowUseCase,
    private val checkAuthStateFlowUseCase: CheckAuthStateUseCase
) : ViewModel() {


    val authState = getAuthStateFlowUseCase()
    fun performAuthResult() {
        viewModelScope.launch { checkAuthStateFlowUseCase() }
    }
}