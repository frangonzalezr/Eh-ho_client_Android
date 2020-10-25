package io.keepcoding.eh_ho.topics

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.keepcoding.eh_ho.R
import kotlinx.android.synthetic.main.fragment_retry.*

class RetryFragment : Fragment() {


    var retryInteractionListener : RetryInteractionListener? = null
    var topicsInteractionListener: TopicsFragment.TopicsInteractionListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is RetryInteractionListener && context is TopicsFragment.TopicsInteractionListener) {
            retryInteractionListener = context
            topicsInteractionListener = context
        } else
            throw IllegalArgumentException("Context doesn't implement ${RetryInteractionListener::class.java.canonicalName}")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_retry, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button.setOnClickListener {
            this.retryInteractionListener?.buttonPressed()
        }
    }

    override fun onDetach() {
        super.onDetach()
        retryInteractionListener = null
    }

    interface RetryInteractionListener {
        fun buttonPressed()
    }
}