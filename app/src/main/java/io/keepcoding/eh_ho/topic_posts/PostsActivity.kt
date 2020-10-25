package io.keepcoding.eh_ho.topic_posts

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import io.keepcoding.eh_ho.R
import io.keepcoding.eh_ho.data.Topic
import io.keepcoding.eh_ho.isFirsTimeCreated
import io.keepcoding.eh_ho.topics.RetryFragment
import kotlinx.android.synthetic.main.activity_login.*

const val EXTRA_TOPIC = "topic"
const val TRANSACTION_CREATE_POST = "create_post"

class PostsActivity : AppCompatActivity(), TopicPostsFragment.PostsInteractionListener,
    RetryFragment.RetryInteractionListener,
    CreateTopicPostFragment.CreatePostInteractionListener
{

    val errorRetryFragment : RetryFragment = RetryFragment()
    var topic : Topic? = null
    val postsFragment : TopicPostsFragment? by lazy {
        topic?.let {
            TopicPostsFragment.newInstance(it)
        }
    }
    val createPostFragment : CreateTopicPostFragment? by lazy {
        topic?.let {
            CreateTopicPostFragment.newInstance(it.id, it.title)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_posts)
        val bundle: Bundle = intent.getBundleExtra("bundle")
        topic = bundle.getParcelable(EXTRA_TOPIC)

        if (isFirsTimeCreated(savedInstanceState))
            postsFragment?.let {
                supportFragmentManager.beginTransaction()
                    .add(R.id.fragmentContainer, it)
                    .commit()
            }
    }

    override fun onCreatePost() {
        createPostFragment?.let {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, it)
                .addToBackStack(TRANSACTION_CREATE_POST)
                .commit()
        }
    }

    override fun postsLoading(enabled: Boolean) {
        if (enabled) {
            fragmentContainer.visibility = View.INVISIBLE
            viewLoading.visibility = View.VISIBLE
        } else {
            fragmentContainer.visibility = View.VISIBLE
            viewLoading.visibility = View.INVISIBLE
        }
    }

    override fun errorsLoading() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, errorRetryFragment)
            .commit()
    }

    override fun buttonPressed() {
        postsFragment?.let {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, it)
                .commit()
        }
    }

    override fun onPostCreated() {
        supportFragmentManager.popBackStack()
    }


}