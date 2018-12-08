package com.lelloman.pdfscores.ui.pdfviewer

import android.arch.lifecycle.MutableLiveData
import android.media.UnsupportedSchemeException
import com.lelloman.common.logger.LoggerFactory
import com.lelloman.common.view.FileProvider
import com.lelloman.common.viewmodel.BaseViewModel
import com.lelloman.pdfrenderer.PdfDocument
import com.lelloman.pdfscores.pdfrenderer.EmptyPdfDocument
import com.lelloman.pdfscores.pdfrenderer.PdfDocumentFactory
import java.io.File
import java.net.URI

class PdfViewerViewModelImpl(
    dependencies: BaseViewModel.Dependencies,
    private val fileProvider: FileProvider,
    loggerFactory: LoggerFactory,
    private val pdfDocumentFactory: PdfDocumentFactory
) : PdfViewerViewModel(dependencies) {

    private val logger = loggerFactory.getLogger(javaClass)

    override val pdfDocument = MutableLiveData<PdfDocument>().apply { value = EmptyPdfDocument }

    override fun setUri(uriString: String) {

        val uri = try {
            URI.create(uriString) ?: throw Exception("null URI")
        } catch (exception: Exception) {
            logger.e("setUri(uriString) called but couldn't parse uriString `$uriString`", exception)
            navigateBack()
            return
        }

        val file = try {
            when (uri.scheme) {
                "asset" -> getTmpAssetFile(uri.path)
                "file" -> fileProvider.getAbsoluteFile(uri.path)
                else -> throw UnsupportedSchemeException("Scheme `${uri.scheme}` is not supported.")
            }
        } catch (exception: Exception) {
            logger.e("Something went wrong when trying to open uri `$uri`", exception)
            navigateBack()
            return
        }

        val document = pdfDocumentFactory.makeFromFile(file)
        pdfDocument.postValue(document)
    }

    private fun getTmpAssetFile(fileName: String): File {
        val assetInputStream = fileProvider.openAssetsFile(fileName.removePrefix("/"))
        val tmpFile = fileProvider.getInternalFile(TMP_FILE_NAME)
        assetInputStream.copyTo(tmpFile.outputStream())
        return tmpFile
    }

    companion object {
        private const val TMP_FILE_NAME = "tmp_asset_copy.pdf"
    }
}