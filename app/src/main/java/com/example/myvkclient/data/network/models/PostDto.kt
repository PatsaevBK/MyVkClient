package com.example.myvkclient.data.network.models

import com.google.gson.annotations.SerializedName

data class PostDto(
    @SerializedName("id") val id: String,
    @SerializedName("type") val type: String,
    @SerializedName("source_id") val communityId: Long,
    @SerializedName("text") val text: String,
    @SerializedName("date") val data: Long,
    @SerializedName("comments") val comments: CommentsDto,
    @SerializedName("likes") val likes: LikesDto,
    @SerializedName("reposts") val reposts: RepostsDto,
    @SerializedName("views") val views: ViewsDto,
    @SerializedName("attachments") val attachments: List<AttachmentDto>?
)
