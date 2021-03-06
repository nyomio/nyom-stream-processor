package com.inepex.nyomagestreamprocessor.httpclient

import org.asynchttpclient.AsyncHttpClient
import org.asynchttpclient.Dsl.*;

class HttpClient {

    private val httpClient = asyncHttpClient(config().setIoThreadsCount(2).setUseInsecureTrustManager(true))

    fun get(): AsyncHttpClient {
        return httpClient
    }
}
