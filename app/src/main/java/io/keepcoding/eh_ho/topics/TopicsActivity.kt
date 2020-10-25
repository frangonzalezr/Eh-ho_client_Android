package io.keepcoding.eh_ho.topics

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import io.keepcoding.eh_ho.*
import io.keepcoding.eh_ho.data.Topic
import io.keepcoding.eh_ho.data.UserRepo
import io.keepcoding.eh_ho.login.LoginActivity
import io.keepcoding.eh_ho.topic_posts.EXTRA_TOPIC
import io.keepcoding.eh_ho.topic_posts.PostsActivity
import kotlinx.android.synthetic.main.activity_login.fragmentContainer
import kotlinx.android.synthetic.main.activity_login.viewLoading

const val TRANSACTION_CREATE_TOPIC = "create_topic"

class TopicsActivity : AppCompatActivity(),
    TopicsFragment.TopicsInteractionListener,
    CreateTopicFragment.CreateTopicInteractionListener,
    RetryFragment.RetryInteractionListener{

    val retryFragment : RetryFragment = RetryFragment()
    val topicsFragment : TopicsFragment = TopicsFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_topics)

        if (isFirsTimeCreated(savedInstanceState))
            supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainer, topicsFragment)
                .commit()
    }

    private fun goToPosts(topic: Topic) {
        val topics: MutableList<Topic> = mutableListOf<Topic>()
        var bundle = Bundle()
        bundle.putParcelable(EXTRA_TOPIC, topic)
        val intent = Intent(this, PostsActivity::class.java)
        intent.putExtra("bundle", bundle)
        startActivity(intent)
    }

    override fun onCreateTopic() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, CreateTopicFragment())
            .addToBackStack(TRANSACTION_CREATE_TOPIC)
            .commit()
    }

    override fun onShowPosts(topic: Topic) {
        goToPosts(topic)
    }

    override fun onTopicCreated() {
        supportFragmentManager.popBackStack()

    }

    override fun onLogout() {
        //Borrar datos
        UserRepo.logout(this.applicationContext)

        //Ir a actividad inicial
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    // PROGRESS BAR

    override fun loadingTopics(enabled: Boolean) {
        if (enabled) {
            fragmentContainer.visibility = View.INVISIBLE
            viewLoading?.visibility = View.VISIBLE
        } else {
            fragmentContainer.visibility = View.VISIBLE
            viewLoading?.visibility = View.INVISIBLE
        }
    }

    // ERRORS

    override fun onErrorLoading() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, retryFragment)
            .commit()
    }

    override fun buttonPressed() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, topicsFragment)
            .commit()
    }
}