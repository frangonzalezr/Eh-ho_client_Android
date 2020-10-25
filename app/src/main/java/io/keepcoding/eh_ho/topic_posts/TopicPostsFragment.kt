package io.keepcoding.eh_ho.topic_posts

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import io.keepcoding.eh_ho.R
import io.keepcoding.eh_ho.data.TopicsPostsRepo
import io.keepcoding.eh_ho.data.Topic
import io.keepcoding.eh_ho.inflate
import kotlinx.android.synthetic.main.fragment_topic_post.*



const val ARG_TOPIC = "topic"

class TopicPostsFragment : Fragment()  {


    var topic: Topic? = null
    var postsInteractionListener: PostsInteractionListener? = null

    private val postsAdapter: TopicPostsAdapter by lazy {
        val adapter = TopicPostsAdapter {

        }
        adapter
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        topic = arguments?.getParcelable<Topic>(ARG_TOPIC)
        if (context is TopicPostsFragment.PostsInteractionListener)
            postsInteractionListener = context
        else
            throw IllegalArgumentException("Context doesn't implement ${PostsInteractionListener::class.java.canonicalName}")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return container?.inflate(R.layout.fragment_topic_post)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        postsAdapter.setPosts(TopicsPostsRepo.posts)

        listPosts.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        listPosts.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        listPosts.adapter = postsAdapter
        context?.let {
            postsSwipeContainer.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(it, R.color.colorPrimary))
            postsSwipeContainer.setOnRefreshListener {
                TopicsPostsRepo.posts.clear()
                loadPosts()
                postsAdapter.setPosts(TopicsPostsRepo.posts)
                listPosts.adapter = postsAdapter
                postsSwipeContainer.isRefreshing = false
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_topic_posts, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onResume() {
        super.onResume()

        loadPosts()
    }

    private fun loadPosts() {
        postsInteractionListener?.postsLoading(true)
        context?.let { it ->
            topic?.id?.let { topicId ->

                TopicsPostsRepo
                    .getPosts(it.applicationContext,
                        topicId,
                        {posts ->
                            postsAdapter.setPosts(posts)
                            postsInteractionListener?.postsLoading(false)
                        },
                        {
                            postsInteractionListener?.postsLoading(false)
                            postsInteractionListener?.errorsLoading()
                        }
                    )
            }
        }
    }

    companion object {
        fun newInstance(topic: Topic): TopicPostsFragment? {
            val fragment = TopicPostsFragment()
            val args = Bundle()
            args.putParcelable(ARG_TOPIC, topic)

            fragment.arguments = args

            return fragment
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_create_post -> this.postsInteractionListener?.onCreatePost()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onDetach() {
        super.onDetach()
        postsInteractionListener = null
    }

    interface PostsInteractionListener {
        fun onCreatePost()
        fun postsLoading(enabled: Boolean)
        fun errorsLoading()
    }
}