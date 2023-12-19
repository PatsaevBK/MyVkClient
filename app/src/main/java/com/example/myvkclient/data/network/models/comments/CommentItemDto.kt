package com.example.myvkclient.data.network.models.comments

import com.google.gson.annotations.SerializedName

data class CommentItemDto(
    @SerializedName("id") val id: Long,
    @SerializedName("from_id") val fromId: Long,
    @SerializedName("date") val date: Long,
    @SerializedName("text") val text: String,
    @SerializedName("post_id") val postId: Long,
    @SerializedName("owner_id") val ownerId: Long,
    @SerializedName("likes") val commentLike: CommentLikeDto
)
