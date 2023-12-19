package com.example.myvkclient.data.network.models.comments

import com.google.gson.annotations.SerializedName

data class CommentsContentDto(
    @SerializedName("count") val count: Int,
    @SerializedName("items") val items: List<CommentItemDto>,
    @SerializedName("profiles") val profiles: List<CommentProfileDto>
)
