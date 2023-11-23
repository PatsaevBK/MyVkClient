package com.example.myvkclient.domain

import com.example.myvkclient.R

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
)
