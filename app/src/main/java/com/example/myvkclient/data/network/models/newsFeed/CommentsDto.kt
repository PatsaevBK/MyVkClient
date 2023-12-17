package com.example.myvkclient.data.network.models.newsFeed

import com.google.gson.annotations.SerializedName

data class CommentsDto(
    @SerializedName("count") val count: Int
)