package com.lelloman.pdfscores.ui

import com.lelloman.common.navigation.DeepLinkStartable
import com.lelloman.common.navigation.NavigationScreen
import com.lelloman.pdfscores.ui.pdfviewer.PdfViewerActivity
import kotlin.reflect.KClass

enum class PdfScoresScreen(
    override val clazz: KClass<*>,
    override val deepLinkStartable: DeepLinkStartable?
) : NavigationScreen {

    PDF_VIEWER(PdfViewerActivity::class, PdfViewerActivity.deepLinkStartable);

    companion object {
        const val EXTRA_PDF_ASSET_FILE_NAME = "EXTRA_PDF_ASSET_FILE_NAME"
    }
}