package com.example.myvkclient.data.mapper

import com.example.myvkclient.data.network.models.comments.CommentItemDto
import com.example.myvkclient.data.network.models.comments.CommentsResponseDto
import com.example.myvkclient.data.network.models.newsFeed.NewsFeedResponseDto
import com.example.myvkclient.domain.entity.FeedPost
import com.example.myvkclient.domain.entity.PostComment
import com.example.myvkclient.domain.entity.PostCommentLikes
import com.example.myvkclient.domain.entity.StatisticItem
import com.example.myvkclient.domain.entity.StatisticType
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.math.absoluteValue

class NewsFeedMapper {

    fun mapResponseToPosts(responseDto: NewsFeedResponseDto): List<FeedPost> {
        val result = linkedSetOf<FeedPost>()
        val newsFeedContentDto = responseDto.newsFeedContentDto
        val posts = newsFeedContentDto.posts.filter { it.type == "post" }
        val groups = newsFeedContentDto.groups

        for ((index, postDto) in posts.withIndex()) {
            /*val group = groups[index] поиск группы реализован через find что
            накладывает алгоритмическую сложность O(n)
            мой вариант - поиск по индексу (O(1)) но это накладывает ограничение на соответствие индексу
            поста и индексу группы*/
            val group = groups.find { it.id == postDto.communityId.absoluteValue } ?: continue
            val attachmentsDto = postDto.attachments
            val feedPost = FeedPost(
                id = postDto.id,
                communityId = postDto.communityId,
                communityName = group.name,
                publicationData = convertTimestampToTime(postDto.data),
                communityImageUrl = group.imageUrl,
                contentText = postDto.text,
                contentImageUrl = attachmentsDto?.firstOrNull()?.photo?.photoUrls?.lastOrNull()?.url,
                statistics = listOf(
                    StatisticItem(StatisticType.SHARES, postDto.reposts.count),
                    StatisticItem(StatisticType.COMMENTS, postDto.comments.count),
                    StatisticItem(StatisticType.LIKES, postDto.likes.count),
                    StatisticItem(StatisticType.VIEWS, postDto.views.count)
                ),
                isFavorite = postDto.likes.userLikes == 1
            )
            result.add(feedPost)
        }
        return result.toList()
    }

    fun mapCommentsResponseDtoToPostComment(
        commentsResponseDto: CommentsResponseDto
    ): List<PostComment> {
        val result = mutableListOf<PostComment>()
        val listOfProfiles = commentsResponseDto.content.profiles
        commentsResponseDto.content.items.forEach { commentItemDto: CommentItemDto ->
            val author = listOfProfiles.firstOrNull { it.id == commentItemDto.fromId }
                ?: throw IllegalStateException("there's no such profile for this comment: $commentItemDto")
            result.add(
                PostComment(
                    id = commentItemDto.id,
                    authorName = author.firstName,
                    authorLastName = author.lastName,
                    authorAvatarUrl = author.photoUri,
                    commentText = commentItemDto.text,
                    publicationTime = convertTimestampToTime(commentItemDto.date),
                    postCommentLikes = with(commentItemDto.commentLike) {
                        PostCommentLikes(
                            count = count,
                            canLike = canLike == 1,
                            userLiked = userLikes == 1
                        )
                    }
                )
            )
        }
        return result.toList()
    }

    private fun convertTimestampToTime(timestamp: Long): String {
        // Создание объекта Timestamp он в конструктор принимает миллисекунды! (умн. на 1000)
        val stamp = Timestamp(timestamp * 1000)
        val date = Date(stamp.time)
        val pattern = choosePattern(timestamp)
        val sdf = SimpleDateFormat(pattern, Locale.getDefault())
        sdf.timeZone = TimeZone.getDefault()
        return sdf.format(date)
    }

    private fun choosePattern(timestamp: Long): String {
        //Выбираем паттерн для даты (со вчерашней нужно добавлять месяц)
        val patternToday = "HH:mm"
        val patternAnotherDay = "dd MMM HH:mm"
        val dayOfPost = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault())
        dayOfPost.timeInMillis = timestamp * 1000
        val today = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault())
        today.timeInMillis = System.currentTimeMillis()
        return if (dayOfPost.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)) {
            patternToday
        } else patternAnotherDay
    }
}