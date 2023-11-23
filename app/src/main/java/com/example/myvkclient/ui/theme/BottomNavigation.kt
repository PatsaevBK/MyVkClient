package com.example.myvkclient.ui.theme

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.SavedStateHandle
import com.example.myvkclient.ViewModel
import com.example.myvkclient.ui.theme.NavigationItem.entries

@Composable
fun BottomPanel(viewModel: ViewModel, bottomState: State<NavigationItem>) {
    val bottomItem by bottomState
    NavigationBar {
        entries.forEach { navigationItem ->
            NavigationBarItem(
                selected = navigationItem == bottomItem,
                onClick = { viewModel.changeBottomState(navigationItem) },
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
