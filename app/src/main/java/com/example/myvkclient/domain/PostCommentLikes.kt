package com.example.myvkclient.domain

data class PostCommentLikes(
    val count: Int,
    val canLike: Boolean,
    val userLiked: Boolean
)
