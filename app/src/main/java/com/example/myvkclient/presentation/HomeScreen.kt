package com.example.myvkclient.presentation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
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
import com.example.myvkclient.domain.FeedPost

@Composable
fun HomeScreen(
    viewModel: ViewModel,
    paddingValues: PaddingValues,
//    navigationState: NavigationState
) {
    val homeScreenState = viewModel.screenState.collectAsState()
    when (val currentState = homeScreenState.value) {
        is HomeScreenState.Posts -> FeedPosts(
            paddingValues,
            currentState,
            viewModel,
//            navigationState
        )

        is HomeScreenState.Comments -> {
            CommentsScreen(
                post = currentState.feedPost,
                comments = currentState.comments,
                onBackPressed = { viewModel.closeComments() }
            )
            BackHandler {
                viewModel.closeComments()
            }
        }
    }

}

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
private fun FeedPosts(
    paddingValues: PaddingValues,
    feedPostState: HomeScreenState.Posts,
    viewModel: ViewModel,
//    navigationState: NavigationState
) {
    LazyColumn(
        modifier = Modifier.padding(paddingValues),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(feedPostState.posts, key = { it.id }) { feedPost: FeedPost ->
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
                    { viewModel.showComments(feedPost) }
                )
            }
        }
    }
}