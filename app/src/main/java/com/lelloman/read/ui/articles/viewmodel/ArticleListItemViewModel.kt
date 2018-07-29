package com.lelloman.read.ui.articles.viewmodel

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.databinding.BaseObservable
import android.databinding.Bindable
import com.lelloman.read.BR
import com.lelloman.read.core.SemanticTimeProvider
import com.lelloman.read.persistence.db.model.SourceArticle
import com.lelloman.read.persistence.settings.AppSettings
import com.lelloman.read.utils.ByteArrayWithId
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable


class ArticleListItemViewModel(
    lifecycle: Lifecycle,
    uiScheduler: Scheduler,
    appSettings: AppSettings,
    private val semanticTimeProvider: SemanticTimeProvider
) : LifecycleObserver, BaseObservable() {

    private val subscription = CompositeDisposable()

    private var imagesEnabled = false

    init {
        lifecycle.addObserver(this)
        subscription.add(appSettings
            .articleListImagesEnabled
            .observeOn(uiScheduler)
            .subscribe { imagesEnabled ->
                this.imagesEnabled = imagesEnabled
                if (imagesEnabled) {
                    imageVisible = !articleImageUrl.isNullOrBlank()
                    imageUrl = articleImageUrl
                } else {
                    imageVisible = false
                    imageUrl = null
                }
                notifyPropertyChanged(BR.imageUrl)
                notifyPropertyChanged(BR.imageVisible)
            }
        )
    }

    var title = ""
        private set

    var details = ""
        private set

    var hash = 0
        private set

    var subtitle = ""
        private set

    @Bindable
    var imageVisible = false
        private set

    @Bindable
    var imageUrl: String? = null
        private set

    private var articleImageUrl: String? = null

    var subtitleVisible = false
        private set

    var faviconVisible = false
        private set

    var favicon: ByteArrayWithId = ByteArrayWithId(null, -1)
        private set

    fun bind(article: SourceArticle) {
        title = article.title
        details = "${semanticTimeProvider.getDateTimeString(article.time)} - ${article.sourceName}"
        hash = article.hashCode()
        subtitle = article.subtitle
        subtitleVisible = article.subtitle.isNotEmpty()
        articleImageUrl = article.imageUrl
        if (imagesEnabled) {
            imageVisible = !articleImageUrl.isNullOrBlank()
            imageUrl = articleImageUrl
        } else {
            imageVisible = false
            imageUrl = null
        }
        faviconVisible = article.favicon != null
        favicon = ByteArrayWithId(article.favicon, article.sourceId)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        subscription.clear()
    }
}