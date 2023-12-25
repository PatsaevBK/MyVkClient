package com.example.myvkclient.domain.entity

data class PostComment(
    val id: Long,
    val authorName: String,
    val authorLastName: String,
    val authorAvatarUrl: String,
    val commentText: String,
    val publicationTime: String,
    val postCommentLikes: PostCommentLikes
)
