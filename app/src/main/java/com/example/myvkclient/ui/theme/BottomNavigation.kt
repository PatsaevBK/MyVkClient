package com.example.myvkclient.ui.theme

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.AccountBox
import androidx.compose.material.icons.sharp.Favorite
import androidx.compose.material.icons.sharp.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.SavedStateHandle
import com.example.myvkclient.R
import com.example.myvkclient.ViewModel
import com.example.myvkclient.ui.theme.NavigationItem.entries

@Composable
fun BottomPanel(viewModel: ViewModel) {
    val stateAll by viewModel.stateMain.collectAsState()
    val chosenNumber = stateAll.bottomNav
    NavigationBar {
        Log.d("BottomPanel", "NavigationBar")
        entries.forEachIndexed { index, navigationItem ->
            NavigationBarItem(
                selected = chosenNumber == index,
                onClick = { viewModel.changeBottomState(index) },
                icon = {
                    Icon(imageVector = navigationItem.icon, contentDescription = null)
                },
                label = {
                    Text(text = stringResource(id = navigationItem.tittleId))
                }
            )
        }
    }
}


@Composable
@Preview
private fun LightTest() {
    MyVkClientTheme {
        BottomPanel(ViewModel(SavedStateHandle()))
    }
}

@Composable
@Preview
private fun DarkTest() {
    MyVkClientTheme(darkTheme = true) {
        BottomPanel(ViewModel(SavedStateHandle()))
    }
}