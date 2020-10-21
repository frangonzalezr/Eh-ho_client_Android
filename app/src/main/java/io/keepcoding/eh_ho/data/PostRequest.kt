package io.keepcoding.eh_ho.data

import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import io.keepcoding.eh_ho.BuildConfig
import org.json.JSONObject

class PostRequest(
    method: Int,
    url: String,
    body: JSONObject?,
    listener: (response: JSONObject?) -> Unit,
    errorListener: (errorResponse: VolleyError) -> Unit,
    private val username: String? = null,
    private val useApiKey: Boolean = true
) : JsonObjectRequest(
    method, url, body, listener, errorListener
) {
    override fun getHeaders(): MutableMap<String, String> {
        val headers = mutableMapOf<String, String>()
        headers["Content-Type"] = "application/json"
        headers["Accept"] = "application/json"

        if(useApiKey)
            headers["Api-Key"] = BuildConfig.DiscourseApiKey

        username?.let {
            headers["Api-Username"] = it
        }


        return headers
    }
}