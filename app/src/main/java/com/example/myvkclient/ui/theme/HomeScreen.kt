package com.example.myvkclient.ui.theme

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.DismissDirection
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myvkclient.ViewModel
import com.example.myvkclient.domain.FeedPost

@Composable
@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
fun HomeScreen(
    viewModel: ViewModel
) {
    val listFeedPostState = viewModel.listFeedPostStateFlow.collectAsState()
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(listFeedPostState.value, key = { it.id }) { feedPost: FeedPost ->
            val dismissState = rememberDismissState()
            if (dismissState.isDismissed(DismissDirection.EndToStart)) {
                viewModel.remove(feedPost)
            }
            SwipeToDismiss(
                state = dismissState,
                modifier = Modifier.animateItemPlacement(),
                dismissThresholds = {
                    FractionalThreshold(0.5f)
                },
                directions = setOf(DismissDirection.EndToStart),
                background = { }
            ) {
                PostCard(
                    feedPost,
                    { statisticItem -> viewModel.updateCount(feedPost, statisticItem) },
                    { statisticItem -> viewModel.updateCount(feedPost, statisticItem) },
                    { statisticItem -> viewModel.updateCount(feedPost, statisticItem) },
                    { statisticItem -> viewModel.updateCount(feedPost, statisticItem) }
                )
            }
        }
    }
}