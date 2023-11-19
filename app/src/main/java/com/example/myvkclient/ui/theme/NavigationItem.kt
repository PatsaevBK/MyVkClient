package com.example.myvkclient.ui.theme

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.myvkclient.R

enum class NavigationItem(
    val tittleId: Int,
    val icon: ImageVector
) {
    HOME(R.string.navigation_item_main, Icons.Filled.Home),
    FAVORITE(R.string.navigation_item_favorite, Icons.Filled.Favorite),
    PROFILE(R.string.navigation_item_profile, Icons.Filled.AccountBox)
}