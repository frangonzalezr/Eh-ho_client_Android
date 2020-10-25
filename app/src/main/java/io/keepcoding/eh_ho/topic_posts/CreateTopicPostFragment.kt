package io.keepcoding.eh_ho.topic_posts

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import io.keepcoding.eh_ho.LoadingDialogFragment
import io.keepcoding.eh_ho.R
import io.keepcoding.eh_ho.data.*
import io.keepcoding.eh_ho.inflate
import kotlinx.android.synthetic.main.fragment_create_topic_post.*
import kotlinx.android.synthetic.main.fragment_create_topic.container
import kotlinx.android.synthetic.main.fragment_create_topic.inputContent
import java.lang.IllegalArgumentException

const val TAG_LOADING_DIALOG = "loading_dialog"

const val TOPIC_ID = "topic_id"

const val ARG_TOPIC_ID = "topic_id"

const val ARG_TOPIC_TITLE = "topic_title"

class CreateTopicPostFragment : Fragment() {

    var interactionListener: CreatePostInteractionListener? = null
    val loadingDialogFragment: LoadingDialogFragment by lazy {
        val message = getString(R.string.creating_post_label)
        LoadingDialogFragment.newInstance(message)
    }

    var topicId: String? = null

    var topicTitle: String? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        topicId = this.arguments?.getString(ARG_TOPIC_ID)

        if (context is CreatePostInteractionListener)
            this.interactionListener = context
        else
            throw IllegalArgumentException("Context doesn't implement ${CreatePostInteractionListener::class.java.canonicalName}")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return container?.inflate(R.layout.fragment_create_topic_post)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        topicTitle = this.arguments?.getString(ARG_TOPIC_TITLE)
        topicTitle?.let {
            labelTopic.text = topicTitle
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_create_topic_post, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_send -> createPost()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
    }

    private fun createPost() {
        if (isFormValid()) {
            postPost()
        } else {
            showErrors()
        }
    }

    private fun postPost() {
        enableLoadingDialog()
        topicId?.let {
            val model = CreateTopicPostModel(
                it,
                inputContent.text.toString()
            )

            context?.let {
                TopicsPostsRepo.addPost(
                    it.applicationContext,
                    model,
                    {
                        enableLoadingDialog(false)
                        interactionListener?.onPostCreated()
                    },
                    {
                        enableLoadingDialog(false)
                        handleError(it)
                    }
                )
            }
        }
    }

    private fun enableLoadingDialog(enabled: Boolean = true) {
        if (enabled)
            loadingDialogFragment.show(childFragmentManager, TAG_LOADING_DIALOG)
        else
            loadingDialogFragment.dismiss()
    }


    private fun handleError(error: RequestError) {
        val message =
            if (error.messageResId != null)
                getString(error.messageResId)
            else error.message ?: getString(R.string.error_default)

        Snackbar.make(container, message, Snackbar.LENGTH_LONG).show()
    }

    private fun showErrors() {
        if (inputContent.text.isEmpty())
            inputContent.error = getString(R.string.error_empty)
    }

    private fun isFormValid() = inputContent.text.isNotEmpty()

    companion object {
        fun newInstance(topicId: String, topicTitle: String): CreateTopicPostFragment? {
            val fragment = CreateTopicPostFragment()
            val args = Bundle()
            args.putString(ARG_TOPIC_ID, topicId)
            args.putString(ARG_TOPIC_TITLE, topicTitle)
            fragment.arguments = args

            return fragment
        }
    }

    interface CreatePostInteractionListener {
        fun onPostCreated()
    }
}