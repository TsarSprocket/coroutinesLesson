package ru.mts.data.main

import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.asResponseBody
import okhttp3.mockwebserver.MockResponse
import ru.mts.data.utils.ResourcesReader
import kotlin.math.roundToInt

class MockInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val uri = chain.request().url.toUri().toString()
        val body =
            MockResponse().setBodyFromFile(
                when {
                    uri.endsWith("api/v1/sample") -> "assets/news.json"
                    else -> ""
                }

            ).getBody()
            ?.asResponseBody("application/json".toMediaTypeOrNull())

        return chain.proceed(chain.request())
            .newBuilder()
            .code(200)
            .protocol(Protocol.HTTP_2)
            .body(
                body
            )
            .addHeader("content-type", "application/json")
            .build()
    }
}

fun MockResponse.setBodyFromFile(filName: String): MockResponse {
    val text = ResourcesReader.readText(filName)
        .replace("%RANDOM%", (Math.random() * 100).roundToInt().toString())
        .replace("%ID%", (Math.random() * 5).roundToInt().toString())
    setBody(text)
    return this
}

