package com.lelloman.pdfscores.pdfrenderer

import android.graphics.Bitmap
import com.lelloman.pdfrenderer.PdfDocument

object EmptyPdfDocument : PdfDocument {
    override val pageCount = 0
    override fun dispose() = Unit
    override fun render(bitmap: Bitmap, pageIndex: Int) = Unit
}