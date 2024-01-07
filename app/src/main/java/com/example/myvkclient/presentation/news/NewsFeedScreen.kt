package com.example.myvkclient.presentation.news

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.DismissDirection
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.Text
import androidx.compose.material.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myvkclient.R
import com.example.myvkclient.domain.entity.FeedPost
import com.example.myvkclient.getApplicationComponent
import com.example.myvkclient.ui.theme.DarkBlue

@Composable
fun NewsFeedScreen(
    paddingValues: PaddingValues,
    onCommentClickListener: (FeedPost) -> Unit
) {
    val component = getApplicationComponent()
    val newsFeedViewModel: NewsFeedViewModel = viewModel(factory = component.getViewModelFactory())
    val homeScreenState = newsFeedViewModel.screenState.collectAsState(NewsFeedScreenState.Initial)
    NewsFeedContent(
        homeScreenState,
        paddingValues,
        newsFeedViewModel,
        onCommentClickListener
    )

}

@Composable
private fun NewsFeedContent(
    homeScreenState: State<NewsFeedScreenState>,
    paddingValues: PaddingValues,
    newsFeedViewModel: NewsFeedViewModel,
    onCommentClickListener: (FeedPost) -> Unit
) {
    when (val currentState = homeScreenState.value) {
        is NewsFeedScreenState.Posts -> {
            FeedPosts(
                paddingValues,
                currentState,
                newsFeedViewModel,
                onCommentClickListener,
                currentState.nextDataIsLoading
            )
        }

        NewsFeedScreenState.Initial -> {

        }

        NewsFeedScreenState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = DarkBlue)
            }
        }

        NewsFeedScreenState.Error -> {
            Toast.makeText(LocalContext.current, "Ошибка загрузки", Toast.LENGTH_SHORT).show()
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Button(onClick = { newsFeedViewModel.loadNextFeedPosts() }) {
                    Text(text = "Попробовать снова")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
private fun FeedPosts(
    paddingValues: PaddingValues,
    feedPostState: NewsFeedScreenState.Posts,
    newsFeedViewModel: NewsFeedViewModel,
    onCommentClickListener: (FeedPost) -> Unit,
    nextDataIsLoading: Boolean
) {
    LazyColumn(
        modifier = Modifier.padding(paddingValues),
        contentPadding = PaddingValues(8.dp, 16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(feedPostState.posts, key = { it.id }) { feedPost: FeedPost ->
            val dismissState = rememberDismissState()
            if (dismissState.isDismissed(DismissDirection.EndToStart)) {
                newsFeedViewModel.ignoreItem(feedPost)
                Toast.makeText(
                    LocalContext.current,
                    stringResource(R.string.ignore_post_toast), Toast.LENGTH_SHORT
                ).show()
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
                    onFeedPostLikeClickListener = {
                        newsFeedViewModel.changeLikeStatus(
                            feedPost
                        )
                    },
                    onFeedPostCommentClickListener = { onCommentClickListener(feedPost) }
                )
            }
        }
        item {
            if (nextDataIsLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = DarkBlue)
                }
            } else {
                SideEffect {
                    newsFeedViewModel.loadNextFeedPosts()
                }
            }
        }
    }
}