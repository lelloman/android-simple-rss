package com.lelloman.simplerss.http

import io.reactivex.Single

interface HttpClient {

    fun request(request: com.lelloman.simplerss.http.HttpRequest): Single<com.lelloman.simplerss.http.HttpResponse>
}