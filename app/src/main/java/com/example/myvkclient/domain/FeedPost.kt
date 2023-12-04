package com.example.myvkclient.domain

import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import com.example.myvkclient.R
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize

@Parcelize
data class FeedPost(
    val id: Int,
    val communityName: String = "dev/null",
    val publicationData: String = "14:00",
    val avatarRestId: Int = R.drawable.post_comunity_thumbnail,
    val contentText: String = "orem ipsum dolor sit amet, consectetur adipiscing elit.",
    val contentImageResId: Int = R.drawable.post_content_image,
    val statistics: List<StatisticItem> = listOf(
        StatisticItem(StatisticType.VIEWS, 966),
        StatisticItem(StatisticType.SHARES, 7),
        StatisticItem(StatisticType.COMMENTS, 8),
        StatisticItem(StatisticType.LIKES, 27)
    )
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
