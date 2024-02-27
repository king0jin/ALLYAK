package com.example.allyak

import android.util.Log
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class SendNotfication {
    var JSON: MediaType? = "application/json; charset=utf-8".toMediaTypeOrNull()

    @Throws(JSONException::class, IOException::class)
    fun sendTopicNotification(token: String?, title: String?, message: String?) {
        val client = OkHttpClient()
        val json = JSONObject()
        val dataJson = JSONObject()
        dataJson.put("body", message)
        dataJson.put("title", title)
        json.put("notification", dataJson)
        json.put("to", token)
        val body: RequestBody = json.toString().toRequestBody(JSON)
        val request: Request = Request.Builder().addHeader("Content-Type", "application/json; UTF-8")
            .addHeader(
                "Authorization",
                "key=AAAACX5mn2E:APA91bG3vtims7qBs1yt6VfolAJf7hxx5CjMh9Ay0dZOAKwzTfwNT4ydP8_RyRZeH_cxwatvUyvfKzCrMzVLnQIJJzUxyrMN52RJMNU4j5lF4FyCHvSEt0r8FBjFAkfMD5H_CnsTwJde"
            )
            .url("https://fcm.googleapis.com/fcm/send")
            .post(body)
            .build()
        val response: Response = client.newCall(request).execute()
        val finalResponse: String = response.body?.string() ?: ""
        Log.i("##INFO", "finalResponse = $finalResponse")

    }

}