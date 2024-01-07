package com.example.myvkclient.presentation.comments

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myvkclient.R
import com.example.myvkclient.domain.entity.FeedPost
import com.example.myvkclient.domain.entity.PostComment
import com.example.myvkclient.getApplicationComponent
import com.example.myvkclient.ui.theme.DarkBlue

@Composable
fun CommentsScreen(
    feedPost: FeedPost,
    onBackPressed: () -> Unit
) {
    val commentsScreenComponent =
        getApplicationComponent()
            .getCommentsScreenComponentFactory()
            .create(feedPost)
    val commentsViewModel: CommentsViewModel =
        viewModel(factory = commentsScreenComponent.getViewModelFactory())
    val screenState = commentsViewModel.screenState.collectAsState(CommentsScreenState.Initial)
    CommentScreenStateContent(
        screenState = screenState,
        commentsViewModel = commentsViewModel,
        onBackPressed = onBackPressed
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun CommentScreenStateContent(
    screenState: State<CommentsScreenState>,
    commentsViewModel: CommentsViewModel,
    onBackPressed: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.Comments)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
                    }
                }

            )
        }
    ) { paddingValues ->
        when (val currentState = screenState.value) {
            is CommentsScreenState.Comments -> {
                Comments(
                    paddingValues,
                    commentsViewModel,
                    currentState.comments,
                    currentState.nextCommentIsLoading,
                    currentState.thatIsAll
                )
            }

            is CommentsScreenState.Loading -> {
                Box(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = DarkBlue)
                }
            }

            CommentsScreenState.Initial -> {

            }
        }
    }
}

@Composable
private fun Comments(
    paddingValues: PaddingValues,
    commentsViewModel: CommentsViewModel,
    listOfItems: List<PostComment>,
    nextCommentIsLoading: Boolean,
    thatIsAll: Boolean
) {
    LazyColumn(
        modifier = Modifier.padding(paddingValues),
        contentPadding = PaddingValues(
            top = 16.dp,
            start = 8.dp,
            end = 8.dp,
            bottom = 72.dp
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(listOfItems, key = { it.id }) {
            CommentCard(
                postComment = it,
                onLikeCommentClickListener = { }
            )
        }
        if (!thatIsAll) {
            item {
                if (nextCommentIsLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = DarkBlue)
                    }
                } else {
                    SideEffect {
                        commentsViewModel.loadNextComments()
                    }
                }
            }
        }
    }
}

