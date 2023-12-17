package com.example.myvkclient.data.network.models.newsFeed

import com.google.gson.annotations.SerializedName

data class LikesCountResponseDto (
    @SerializedName("response") val response: LikesCountDto
)

data class LikesCountDto (
    @SerializedName("likes") val count: Int
)