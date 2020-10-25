package io.keepcoding.eh_ho.topic_posts
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.keepcoding.eh_ho.R
import io.keepcoding.eh_ho.data.TopicPost
import io.keepcoding.eh_ho.inflate
import kotlinx.android.synthetic.main.item_topic_post.view.*
import java.lang.IllegalArgumentException

class TopicPostsAdapter(val postClickListener: ((TopicPost) -> Unit)? = null) :
    RecyclerView.Adapter<TopicPostsAdapter.PostHolder>() {

    private val posts = mutableListOf<TopicPost>()

    private val listener: ((View) -> Unit) = {
        if (it.tag is TopicPost) {
            postClickListener?.invoke(it.tag as TopicPost)
        } else {
            throw IllegalArgumentException("Post item view has not a Post Data as a tag")
        }

    }

    override fun getItemCount(): Int {
        return posts.size
    }

    override fun onCreateViewHolder(list: ViewGroup, viewType: Int): PostHolder {
        val view = list.inflate(R.layout.item_topic_post)
        return PostHolder(view)
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        val post = posts[position]
        holder.post = post
        holder.itemView.setOnClickListener(listener)
    }

    fun setPosts(post: List<TopicPost>) {
        this.posts.clear()
        this.posts.addAll(post)
        notifyDataSetChanged()
    }

    inner class PostHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var post: TopicPost? = null
            set(value) {
                field = value
                itemView.tag = field

                field?.let {
                    itemView.labelAuthor.text = it.author
                    itemView.labelContent.text = it.content.toString()
                    itemView.labelDate.text = it.date.toString()
                }
            }
    }

}
