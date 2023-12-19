package com.example.myvkclient.data.network.models.newsFeed

import com.google.gson.annotations.SerializedName

data class ResponseIgnoreDto(
    @SerializedName("response") val ignoreStatusDto: IgnoreStatusDto,
)
