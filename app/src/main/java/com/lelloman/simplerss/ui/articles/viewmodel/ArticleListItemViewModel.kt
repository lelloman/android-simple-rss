package com.lelloman.simplerss.ui.articles.viewmodel

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.lelloman.common.utils.model.ByteArrayWithId
import com.lelloman.common.view.SemanticTimeProvider
import com.lelloman.common.viewmodel.BaseListItemViewModel
import com.lelloman.simplerss.BR
import com.lelloman.simplerss.persistence.db.model.SourceArticle
import com.lelloman.simplerss.persistence.settings.AppSettings
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable


class ArticleListItemViewModel(
    lifecycle: Lifecycle,
    uiScheduler: Scheduler,
    appSettings: AppSettings,
    private val semanticTimeProvider: SemanticTimeProvider
) : LifecycleObserver, BaseListItemViewModel<Long, SourceArticle>, BaseObservable() {

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

    override fun bind(item: SourceArticle) {
        title = item.title
        details = "${semanticTimeProvider.getDateTimeString(item.time)} - ${item.sourceName}"
        hash = item.hashCode()
        subtitle = item.subtitle
        subtitleVisible = item.subtitle.isNotEmpty()
        articleImageUrl = item.imageUrl
        if (imagesEnabled) {
            imageVisible = !articleImageUrl.isNullOrBlank()
            imageUrl = articleImageUrl
        } else {
            imageVisible = false
            imageUrl = null
        }
        faviconVisible = item.favicon != null
        favicon = ByteArrayWithId(item.favicon, item.sourceId)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        subscription.clear()
    }
}