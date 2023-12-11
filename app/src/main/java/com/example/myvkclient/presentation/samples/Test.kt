package com.example.myvkclient.presentation.samples

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun Test1() {
    Scaffold(
        topBar = { TopAppBar(title = { Text(text = "Scaffold1") }) }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            var checked by remember {
                mutableStateOf(false)
            }
            Checkbox(checked = checked, onCheckedChange = {
                checked = it
            })
            when (checked) {
                true -> {
                    Scaffold(
                        topBar = { TopAppBar(title = { Text(text = "Scaffold2") }) }
                    ) {
                        Text(text = "Text", modifier = Modifier.padding(it))
                    }
                }

                false -> { Text(text = "1") }
            }
        }
    }
}