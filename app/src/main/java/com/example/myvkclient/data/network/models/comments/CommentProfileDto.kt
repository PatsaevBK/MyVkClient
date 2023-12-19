package com.example.myvkclient.data.network.models.comments

import com.google.gson.annotations.SerializedName

data class CommentProfileDto(
    @SerializedName("id") val id: Long,
    @SerializedName("first_name") val firstName: String,
    @SerializedName("last_name") val lastName: String,
    @SerializedName("photo_100") val photoUri: String
)
