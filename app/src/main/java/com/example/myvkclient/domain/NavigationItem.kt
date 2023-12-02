package com.example.myvkclient.domain

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.myvkclient.R
import com.example.myvkclient.navigation.Screen

enum class NavigationItem(
    val screen: Screen,
    val tittleId: Int,
    val icon: ImageVector
) {
    HOME(Screen.Home, R.string.navigation_item_main, Icons.Filled.Home),
    FAVORITE(Screen.Favourite, R.string.navigation_item_favorite, Icons.Filled.Favorite),
    PROFILE(Screen.Profile, R.string.navigation_item_profile, Icons.Filled.AccountBox)
}