package com.lelloman.pdfscores.pdfrenderer

import com.lelloman.pdfrenderer.PdfDocument
import java.io.File

interface PdfDocumentFactory {

    fun make(file: File): PdfDocument
}