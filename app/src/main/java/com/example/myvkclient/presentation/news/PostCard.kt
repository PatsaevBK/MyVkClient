package com.example.myvkclient.presentation.news

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.myvkclient.R
import com.example.myvkclient.domain.FeedPost
import com.example.myvkclient.domain.StatisticItem
import com.example.myvkclient.domain.StatisticType
import com.example.myvkclient.ui.theme.DarkRed

@Composable
fun PostCard(
    feedPost: FeedPost,
    onFeedPostLikeClickListener: (StatisticItem) -> Unit,
    onFeedPostShareClickListener: (StatisticItem) -> Unit,
    onFeedPostViewsClickListener: (StatisticItem) -> Unit,
    onFeedPostCommentClickListener: (StatisticItem) -> Unit
) {
    Card {
        Column(modifier = Modifier.padding(8.dp)) {
            PostHeader(feedPost)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = feedPost.contentText,
            )
            Spacer(modifier = Modifier.height(8.dp))
            AsyncImage(
                model = feedPost.contentImageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth(),
                contentScale = ContentScale.FillWidth
            )
            Spacer(modifier = Modifier.height(8.dp))
            Statistics(
                feedPost,
                onLikeClickListener = onFeedPostLikeClickListener,
                onShareClickListener = onFeedPostShareClickListener,
                onViewsClickListener = onFeedPostViewsClickListener,
                onCommentClickListener = onFeedPostCommentClickListener
            )
        }
    }
}

@Composable
private fun Statistics(
    feedPost: FeedPost,
    onLikeClickListener: (StatisticItem) -> Unit,
    onShareClickListener: (StatisticItem) -> Unit,
    onViewsClickListener: (StatisticItem) -> Unit,
    onCommentClickListener: (StatisticItem) -> Unit,
) {
    val statistics = feedPost.statistics
    val isFavorite = feedPost.isFavorite
    Row(
        modifier = Modifier
            .padding(start = 8.dp, end = 8.dp, bottom = 4.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(modifier = Modifier.weight(1f)) {
            val viewsItem = statistics.getItemByType(StatisticType.VIEWS)
            IconWithText(
                R.drawable.ic_views_count,
                convertStatisticsToString(viewsItem.count),
                onItemClickListener = {
                    onViewsClickListener(viewsItem)
                }
            )
        }
        Row(modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.SpaceBetween) {
            val shareItem = statistics.getItemByType(StatisticType.SHARES)
            IconWithText(
                R.drawable.ic_share,
                convertStatisticsToString(shareItem.count),
                onItemClickListener = {
                    onShareClickListener(shareItem)
                }
            )
            val commentItem = statistics.getItemByType(StatisticType.COMMENTS)
            IconWithText(
                R.drawable.ic_comment,
                convertStatisticsToString(commentItem.count),
                onItemClickListener = {
                    onCommentClickListener(commentItem)
                }
            )
            val likeItem = statistics.getItemByType(StatisticType.LIKES)
            val favoriteIconId = if (isFavorite) R.drawable.ic_like else R.drawable.ic_unlike
            IconWithText(
                favoriteIconId,
                convertStatisticsToString(likeItem.count),
                if (isFavorite) DarkRed else Color.Unspecified,
                onItemClickListener = {
                    onLikeClickListener(likeItem)
                }
            )
        }
    }
}

private fun convertStatisticsToString(count: Int): String {
    return when {
        count > 100_000 -> "${count / 1000}К"
        count in 1000..100_000 -> String.format("%.1fК", count / 1000f)
        else -> "$count"
    }
}

private fun List<StatisticItem>.getItemByType(type: StatisticType): StatisticItem {
    return this.find { it.type == type }
        ?: throw IllegalStateException("No such type: $type in statistics list")
}

@Composable
private fun IconWithText(
    idResource: Int,
    text: String,
    tint: Color = Color.Unspecified,
    onItemClickListener: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable(onClick = onItemClickListener)
    ) {
        Icon(
            painter = painterResource(id = idResource),
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = text)
    }
}

@Composable
private fun PostHeader(feedPost: FeedPost) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AsyncImage(
            model = feedPost.communityImageUrl,
            contentDescription = null,
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(text = feedPost.communityName/* color = MaterialTheme.colorScheme.onPrimary*/)
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = feedPost.publicationData /*color = MaterialTheme.colorScheme.onSecondary*/)
        }
        Icon(imageVector = Icons.Rounded.MoreVert, contentDescription = null)
    }
}

/*
@Composable
@Preview
private fun DarkThem() {
    val feedPost = FeedPost(0)
    MyVkClientTheme(true) {
        PostCard(feedPost, { }, { }, { }, { })
    }
}

@Composable
@Preview
private fun LightThem() {
    val feedPost = FeedPost(0)
    MyVkClientTheme(false) {
        PostCard(feedPost, { }, { }, { }, { })
    }
}*/
