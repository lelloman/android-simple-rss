package com.lelloman.pdfscores.recentscores

import com.lelloman.pdfscores.persistence.PdfScore

class PdfScoreViewModelItem(
    pdfScore: PdfScore
) : PdfScore by pdfScore {
}