package com.lelloman.pdfscores.publicapi

import io.reactivex.Observable

interface PublicPdfScoresAppsFinder {

    val pdfScoresApps: Observable<List<PublicPdfScoresApp>>
}