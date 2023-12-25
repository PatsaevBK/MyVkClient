package com.example.myvkclient.presentation.comments

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.myvkclient.R
import com.example.myvkclient.domain.entity.PostComment

@Composable
fun CommentCard(postComment: PostComment, onLikeCommentClickListener: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp,
                vertical = 4.dp
            ),
        verticalAlignment = Alignment.Top
    ) {
        AsyncImage(
            model = postComment.authorAvatarUrl,
            contentDescription = null,
            modifier = Modifier
                .size(48.dp)
                .clip(
                    CircleShape
                )

        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            Text(
                text = "${postComment.authorName} ${postComment.authorLastName}",
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = postComment.commentText,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            if (postComment.postCommentLikes.count > 0) {
                val idResourceOfIcon = if (postComment.postCommentLikes.userLiked) {
                    R.drawable.ic_like
                } else R.drawable.ic_unlike
                IconWithText(
                    idResource = idResourceOfIcon,
                    text = postComment.postCommentLikes.count.toString(),
                    onItemClickListener = onLikeCommentClickListener
                )
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = postComment.publicationTime,
            fontSize = 12.sp
        )
    }
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
            modifier = Modifier.size(15.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = text, fontSize = 15.sp)
    }
}