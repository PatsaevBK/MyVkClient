package com.example.myvkclient.domain

import com.example.myvkclient.R

data class PostComment(
    val id: Int,
    val authorName: String,
    val authorAvatarId: Int = R.drawable.avatar,
    val commentText: String = "Long comment Text",
    val publicationTime: String = "14:00"
)
