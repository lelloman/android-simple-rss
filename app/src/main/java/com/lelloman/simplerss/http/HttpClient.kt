package com.lelloman.simplerss.http

import io.reactivex.Single

interface HttpClient {

    fun request(request: HttpRequest): Single<HttpResponse>
}