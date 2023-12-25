package com.example.myvkclient.presentation.main

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.myvkclient.domain.entity.NavigationItem.entries
import com.example.myvkclient.navigation.NavigationState

@Composable
fun BottomBar(navigationState: NavigationState) {
    val navBackStackEntry by navigationState.navHostController.currentBackStackEntryAsState()
    NavigationBar {
        entries.forEach { navigationItem ->
            val selected = navBackStackEntry?.destination?.hierarchy?.any {
                it.route == navigationItem.screen.route
            } ?: false
            NavigationBarItem(
                selected = selected,
                onClick = {
                    if (!selected) {
                        navigationState.navigateTo(navigationItem.screen.route)
                    }
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
