package com.example.myvkclient.data.network.models.comments

import com.google.gson.annotations.SerializedName

data class CommentLikeDto(
    @SerializedName("can_like") val canLike: Int,
    @SerializedName("count") val count: Int,
    @SerializedName("user_likes") val userLikes: Int,
)
