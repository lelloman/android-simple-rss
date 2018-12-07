package com.lelloman.pdfscores.ui.pdfviewer

import android.content.Context
import android.content.Intent
import android.os.ParcelFileDescriptor
import com.lelloman.common.navigation.DeepLink
import com.lelloman.common.navigation.DeepLinkStartable
import com.lelloman.common.view.BaseActivity
import com.lelloman.pdfrenderer.PdfDocumentImpl
import com.lelloman.pdfscores.R
import com.lelloman.pdfscores.databinding.ActivityPdfViewerBinding
import com.lelloman.pdfscores.ui.PdfScoresScreen.Companion.EXTRA_PDF_ASSET_FILE_NAME
import java.io.File

class PdfViewerActivity : BaseActivity<PdfViewerViewModel, ActivityPdfViewerBinding>() {

    override val layoutResId = R.layout.activity_pdf_viewer

    override fun setViewModel(binding: ActivityPdfViewerBinding, viewModel: PdfViewerViewModel) {
        binding.viewModel = viewModel
        val file = File(filesDir, "tmp")
        if (file.exists()) {
            file.delete()
        }
        assets.open(intent.getStringExtra(EXTRA_PDF_ASSET_FILE_NAME)).buffered().copyTo(file.outputStream())
        val pdf = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
        binding.pdfView.setPdfDocument(PdfDocumentImpl(pdf))
    }

    override fun getViewModelClass() = PdfViewerViewModel::class.java

    companion object {
        val deepLinkStartable = object : DeepLinkStartable {
            override fun start(context: Context, deepLink: DeepLink) {
                val intent = Intent(context, PdfViewerActivity::class.java)
                    .putExtra(EXTRA_PDF_ASSET_FILE_NAME, deepLink.getString(EXTRA_PDF_ASSET_FILE_NAME))
                context.startActivity(intent)
            }
        }
    }
}