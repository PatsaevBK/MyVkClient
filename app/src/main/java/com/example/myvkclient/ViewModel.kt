package com.example.myvkclient

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.myvkclient.ui.theme.StateAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {

    private val _stateMain = MutableStateFlow(StateAll(0))
    val stateMain = _stateMain.asStateFlow()

    fun changeBottomState(i: Int) {
        _stateMain.apply {
            val new = value.copy(bottomNav = i)
            value = new
        }
    }

    companion object {
        val Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                val savedStateHandle = extras.createSavedStateHandle()
                return ViewModel(savedStateHandle) as T
            }
        }
    }
}