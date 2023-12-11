package com.example.myvkclient.presentation.samples

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

@Composable
fun SideEffectTest(number: Number) {
    val someState = remember {
        mutableStateOf(false)
    }
    Column {
        log("Recomposition ${someState.value}")
        LazyColumn(content = {
            items(5) {
                Text(text = "Number: ${number.i}")
            }
        })
        SideEffect {
            log("SIDEEFFECT")
            number.i = 10
        }
        LazyColumn(content = {
            items(5) {
                Text(text = "Number: ${number.i}")
            }
        })
        Button(onClick = { someState.value = !someState.value }) {
            Text(text = "Change state")
        }
    }
}

data class Number(var i: Int)

private fun log(message: String) {
    Log.d("SideEffect", message)
}