package com.lelloman.pdfscores.pdfrenderer

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PdfRendererModule {

    @Provides
    @Singleton
    fun providePdfDocumentFactory(): PdfDocumentFactory = PdfDocumentFactoryImpl()
}