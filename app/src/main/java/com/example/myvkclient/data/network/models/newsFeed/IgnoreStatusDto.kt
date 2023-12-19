package com.example.myvkclient.data.network.models.newsFeed

import com.google.gson.annotations.SerializedName

data class IgnoreStatusDto(
    @SerializedName("status") val status: Boolean
)
