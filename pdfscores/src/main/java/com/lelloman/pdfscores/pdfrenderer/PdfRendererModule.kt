package com.lelloman.pdfscores.pdfrenderer

import com.lelloman.pdfrenderer.PdfDocument
import dagger.Module
import dagger.Provides
import java.io.File
import javax.inject.Singleton

@Module
class PdfRendererModule {

    @Provides
    @Singleton
    fun providePdfDocumentFactory(): PdfDocumentFactory = object  :PdfDocumentFactory {
        override fun make(file: File): PdfDocument  = com.lelloman.pdfrenderer.PdfDocumentFactory.make(file)
    }
}