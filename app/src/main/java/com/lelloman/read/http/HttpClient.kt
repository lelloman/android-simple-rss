package com.lelloman.read.http

import io.reactivex.Single

interface HttpClient {

    fun request(request: HttpRequest): Single<HttpResponse>
}