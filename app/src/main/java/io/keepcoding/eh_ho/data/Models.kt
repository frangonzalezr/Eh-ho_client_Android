package io.keepcoding.eh_ho.data

import android.os.Parcelable
import android.text.Html
import kotlinx.android.parcel.Parcelize
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
@Parcelize
data class Topic(
    val id: String = UUID.randomUUID().toString(),
    val title: String = "",
    val date: Date = Date(),
    val posts: Int = 0,
    val views: Int = 0
) : Parcelable {

    companion object {
        fun parseTopicsList(response: JSONObject): List<Topic> {
            val objectList = response.getJSONObject("topic_list")
                .getJSONArray("topics")

            val topics = mutableListOf<Topic>()

            for (i in 0 until objectList.length()) {
                val parsedTopic = parseTopic(objectList.getJSONObject(i))
                topics.add(parsedTopic)
            }

            return topics
        }

        fun parseTopic(jsonObject: JSONObject): Topic {
            val date = jsonObject.getString("created_at")
                .replace("Z", "+0000")

            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault())
            val dateFormatted = dateFormat.parse(date) ?: Date()

            return Topic(
                id = jsonObject.getInt("id").toString(),
                title = jsonObject.getString("title").toString(),
                date = dateFormatted,
                posts = jsonObject.getInt("posts_count"),
                views = jsonObject.getInt("views")
            )
        }
    }

    val MINUTE_MILLIS = 1000L * 60
    val HOUR_MILLIS = MINUTE_MILLIS * 60
    val DAY_MILLIS = HOUR_MILLIS * 24
    val MONTH_MILLIS = DAY_MILLIS * 30
    val YEAR_MILLIS = MONTH_MILLIS * 12

    data class TimeOffset(val amount: Int, val unit: Int)

    /**
     * Fecha de creación de topico '01/01/2020 10:00:00'
     * @param Date Fecha de consulta '01/01/2020 11:00:00'
     * @return { unit: "Hora", amount: 1 }
     **/
    fun getTimeOffset(dateToCompare: Date = Date()): TimeOffset {
        val current = dateToCompare.time
        val diff = current - this.date.time

        val years = diff / YEAR_MILLIS
        if (years > 0) return TimeOffset(
            years.toInt(),
            Calendar.YEAR
        )

        val months = diff / MONTH_MILLIS
        if (months > 0) return TimeOffset(
            months.toInt(),
            Calendar.MONTH
        )

        val days = diff / DAY_MILLIS
        if (days > 0) return TimeOffset(
            days.toInt(),
            Calendar.DAY_OF_MONTH
        )

        val hours = diff / HOUR_MILLIS
        if (hours > 0) return TimeOffset(
            hours.toInt(),
            Calendar.HOUR
        )

        val minutes = diff / MINUTE_MILLIS
        if (minutes > 0) return TimeOffset(
            minutes.toInt(),
            Calendar.MINUTE
        )

        return TimeOffset(0, Calendar.MINUTE)
    }
}

data class TopicPost(
    val id: String = UUID.randomUUID().toString(),
    val author: String= "",
    val content: String = "",
    val date: String = ""
) {
    companion object {
        fun parsePostsList(response: JSONObject) : List<TopicPost> {
            val objectList = response.getJSONObject("post_stream")
                .getJSONArray("posts")
            val posts = mutableListOf<TopicPost>()

            for (i in 0 until objectList.length()) {
                val parsedPost = parsePost(objectList.getJSONObject(i))
                posts.add(parsedPost)
            }
            return posts
        }
        fun parsePost(jsonObject: JSONObject): TopicPost {
            val date = jsonObject.getString("created_at")
                .replace("Z", "+0000")

            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault())
            val dateFormatted = dateFormat.parse(date) ?: Date()
            val dateFormatString = SimpleDateFormat("yyyy-MM-dd")
            val dateFormattedString = dateFormatString.format(dateFormatted)
            return TopicPost(
                id = jsonObject.getString("id"),
                author = jsonObject.getString("username"),
                content = Html.fromHtml(jsonObject.getString("cooked")).toString(),
                date = dateFormattedString
            )
        }
    }
}