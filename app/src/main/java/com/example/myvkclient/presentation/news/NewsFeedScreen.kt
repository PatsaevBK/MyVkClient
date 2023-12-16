package com.example.myvkclient.presentation.news

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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myvkclient.domain.FeedPost

@Composable
fun NewsFeedScreen(
    paddingValues: PaddingValues,
    onCommentClickListener: (FeedPost) -> Unit
) {
    val newsFeedViewModel: NewsFeedViewModel = viewModel(factory = NewsFeedViewModel.Factory)
    val homeScreenState = newsFeedViewModel.screenState.collectAsState()
    when (val currentState = homeScreenState.value) {
        is NewsFeedScreenState.Posts -> FeedPosts(
            paddingValues,
            currentState,
            newsFeedViewModel,
            onCommentClickListener
        )
        is NewsFeedScreenState.Initial -> {

        }
    }

}

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
private fun FeedPosts(
    paddingValues: PaddingValues,
    feedPostState: NewsFeedScreenState.Posts,
    newsFeedViewModel: NewsFeedViewModel,
    onCommentClickListener: (FeedPost) -> Unit
) {
    LazyColumn(
        modifier = Modifier.padding(paddingValues),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(feedPostState.posts, key = { it.id }) { feedPost: FeedPost ->
            val dismissState = rememberDismissState()
            if (dismissState.isDismissed(DismissDirection.EndToStart)) {
                newsFeedViewModel.remove(feedPost)
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
                    feedPost = feedPost,
                    onFeedPostLikeClickListener = { statisticItem -> newsFeedViewModel.updateCount(feedPost, statisticItem) },
                    onFeedPostShareClickListener = { statisticItem -> newsFeedViewModel.updateCount(feedPost, statisticItem) },
                    onFeedPostViewsClickListener = { statisticItem -> newsFeedViewModel.updateCount(feedPost, statisticItem) },
                    onFeedPostCommentClickListener = { onCommentClickListener(feedPost) }
                )
            }
        }
    }
}