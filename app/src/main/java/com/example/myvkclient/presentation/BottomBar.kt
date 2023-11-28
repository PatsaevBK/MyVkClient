package com.example.myvkclient.presentation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.myvkclient.domain.NavigationItem.entries
import com.example.myvkclient.navigation.NavigationState

@Composable
fun BottomBar(navigationState: NavigationState) {
    val navBackStackEntry by navigationState.navHostController.currentBackStackEntryAsState()
    val currentRout = navBackStackEntry?.destination?.route
    NavigationBar {
        entries.forEach { navigationItem ->
            NavigationBarItem(
                selected = navigationItem.screen.route == currentRout,
                onClick = {
                    navigationState.navigateTo(navigationItem.screen.route)
                },
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
