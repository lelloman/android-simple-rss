package com.lelloman.pdfscores.pdfrenderer

import android.os.ParcelFileDescriptor
import com.lelloman.pdfrenderer.PdfDocument
import com.lelloman.pdfrenderer.PdfDocumentImpl
import java.io.File

class PdfDocumentFactoryImpl : PdfDocumentFactory {

    override fun makeFromFile(file: File): PdfDocument = ParcelFileDescriptor
        .open(file, ParcelFileDescriptor.MODE_READ_ONLY)
        .let(::PdfDocumentImpl)

}