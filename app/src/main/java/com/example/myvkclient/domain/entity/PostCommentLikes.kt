package com.example.myvkclient.domain.entity

data class PostCommentLikes(
    val count: Int,
    val canLike: Boolean,
    val userLiked: Boolean
)
