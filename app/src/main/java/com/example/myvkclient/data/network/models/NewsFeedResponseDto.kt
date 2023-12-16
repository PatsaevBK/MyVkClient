package com.example.myvkclient.data.network.models

import com.google.gson.annotations.SerializedName

data class NewsFeedResponseDto(
    @SerializedName("response") val newsFeedContentDto: NewsFeedContentDto
)
