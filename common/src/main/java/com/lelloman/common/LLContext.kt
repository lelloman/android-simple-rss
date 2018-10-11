package com.lelloman.common

import android.content.Context
import android.content.ContextWrapper
import android.net.Uri
import com.lelloman.common.logger.LoggerFactory
import com.lelloman.common.utils.TimeProvider
import com.lelloman.common.utils.UrlValidator
import com.lelloman.common.view.ContentUriOpener
import com.lelloman.common.view.MeteredConnectionChecker
import com.lelloman.common.view.PicassoWrap
import com.lelloman.common.view.ResourceProvider
import com.lelloman.common.view.SemanticTimeProvider
import io.reactivex.Scheduler
import java.io.InputStream

class LLContext(
    context: Context,
    timeProvider: TimeProvider,
    val uiScheduler: Scheduler,
    val ioScheduler: Scheduler,
    val newThreadScheduler: Scheduler,
    loggerFactory: LoggerFactory,
    meteredConnectionChecker: MeteredConnectionChecker,
    urlValidator: UrlValidator,
    picassoWrap: PicassoWrap,
    semanticTimeProvider: SemanticTimeProvider
) : ContextWrapper(context),
    TimeProvider by timeProvider,
    LoggerFactory by loggerFactory,
    UrlValidator by urlValidator,
    MeteredConnectionChecker by meteredConnectionChecker,
    PicassoWrap by picassoWrap,
    SemanticTimeProvider by semanticTimeProvider,
    ResourceProvider,
    ContentUriOpener {

    override fun getStringArray(arrayId: Int): Array<String> {
        return resources.getStringArray(arrayId)
    }

    override fun open(uri: Uri): InputStream? = contentResolver.openInputStream(uri)
}