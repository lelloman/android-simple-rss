package com.lelloman.pdfscores.ui.pdfscoreslist

import com.lelloman.pdfscores.persistence.model.PdfScore

class PdfScoreViewModelItem(
    pdfScore: PdfScore
) : PdfScore by pdfScore {
}