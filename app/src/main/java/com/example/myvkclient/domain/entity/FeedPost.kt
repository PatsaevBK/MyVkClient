package com.example.myvkclient.domain.entity

import android.os.Bundle
import android.os.Parcelable
import androidx.compose.runtime.Immutable
import androidx.navigation.NavType
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize

@Immutable
@Parcelize
data class FeedPost(
    val id: Long,
    val communityId: Long,
    val communityName: String,
    val publicationData: String,
    val communityImageUrl: String,
    val contentText: String,
    val contentImageUrl: String?,
    val statistics: List<StatisticItem>,
    val isFavorite: Boolean
) : Parcelable {



    companion object {
        val NavigationType = object : NavType<FeedPost>(false) {

            override fun get(bundle: Bundle, key: String): FeedPost? {
                return bundle.getParcelable<FeedPost>(key)
            }

            override fun parseValue(value: String): FeedPost {
                return  Gson().fromJson(value, FeedPost::class.java)
            }

            override fun put(bundle: Bundle, key: String, value: FeedPost) {
                bundle.putParcelable(key, value)
            }
        }
    }
}
