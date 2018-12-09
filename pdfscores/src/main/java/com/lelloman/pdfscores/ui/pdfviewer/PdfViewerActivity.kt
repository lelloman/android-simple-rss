package com.lelloman.pdfscores.ui.pdfviewer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View.*
import com.lelloman.common.navigation.DeepLink
import com.lelloman.common.navigation.DeepLinkStartable
import com.lelloman.common.view.BaseActivity
import com.lelloman.pdfscores.R
import com.lelloman.pdfscores.databinding.ActivityPdfViewerBinding
import com.lelloman.pdfscores.ui.PdfScoresScreen.Companion.EXTRA_PDF_URI
import java.io.File

class PdfViewerActivity : BaseActivity<PdfViewerViewModel, ActivityPdfViewerBinding>() {

    override val layoutResId = R.layout.activity_pdf_viewer

    override val hasActionBar = false

    override fun setViewModel(binding: ActivityPdfViewerBinding, viewModel: PdfViewerViewModel) {
        binding.viewModel = viewModel
        val file = File(filesDir, "tmp")
        if (file.exists()) {
            file.delete()
        }
        val pdfUri = intent.getStringExtra(EXTRA_PDF_URI)
        viewModel.setUri(pdfUri)
    }

    override fun getViewModelClass() = PdfViewerViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = SYSTEM_UI_FLAG_IMMERSIVE_STICKY or SYSTEM_UI_FLAG_FULLSCREEN or SYSTEM_UI_FLAG_HIDE_NAVIGATION
    }

    companion object {
        val deepLinkStartable = object : DeepLinkStartable {
            override fun start(context: Context, deepLink: DeepLink) {
                val intent = Intent(context, PdfViewerActivity::class.java)
                    .putExtra(EXTRA_PDF_URI, deepLink.getString(EXTRA_PDF_URI))
                context.startActivity(intent)
            }
        }
    }
}