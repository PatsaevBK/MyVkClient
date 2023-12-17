package com.example.myvkclient.data.network.models.newsFeed

import com.google.gson.annotations.SerializedName

data class ResponseIgnore(
    @SerializedName("response") val ignoreStatus: IgnoreStatus,
)
