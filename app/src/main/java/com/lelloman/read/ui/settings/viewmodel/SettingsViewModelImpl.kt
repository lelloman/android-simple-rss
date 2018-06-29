package com.lelloman.read.ui.settings.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.databinding.Observable
import android.databinding.ObservableField
import com.lelloman.read.core.ResourceProvider
import com.lelloman.read.core.SemanticTimeProvider
import com.lelloman.read.core.di.qualifiers.IoScheduler
import com.lelloman.read.core.di.qualifiers.UiScheduler
import com.lelloman.read.persistence.settings.AppSettings
import com.lelloman.read.persistence.settings.SourceRefreshInterval
import com.lelloman.read.utils.LazyLiveData
import io.reactivex.Scheduler

class SettingsViewModelImpl(
    @IoScheduler private val ioScheduler: Scheduler,
    @UiScheduler private val uiScheduler: Scheduler,
    resourceProvider: ResourceProvider,
    private val appSettings: AppSettings,
    semanticTimeProvider: SemanticTimeProvider
) : SettingsViewModel(resourceProvider) {

    private val sortedRefreshIntervals = SourceRefreshInterval
        .values()
        .sortedBy { it.ms }

    override val minRefreshIntervals: MutableLiveData<List<String>> by LazyLiveData {
        subscription {
            appSettings.sourceRefreshMinInterval
                .observeOn(uiScheduler)
                .subscribe {
                    selectedMinRefreshInterval.set(sortedRefreshIntervals.indexOf(it))
                }
        }
        minRefreshIntervals.value = sortedRefreshIntervals.map {
            semanticTimeProvider.getTimeQuantity(it.ms)
        }
    }

    override val selectedMinRefreshInterval = ObservableField<Int>().also {
        it.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                if (sender is ObservableField<*>) {
                    (sender.get() as? Int)?.let { position ->
                        appSettings.setSourceRefreshMinInterval(sortedRefreshIntervals[position])
                    }
                }
            }
        })
    }

    override val articlesListImagesSelected: MutableLiveData<Boolean> by LazyLiveData {
        subscription {
            appSettings
                .articleListImagesEnabled
                .subscribe { articlesListImagesSelected.value = it }

        }
    }

    override val useMeteredNetworkSelected: MutableLiveData<Boolean> by LazyLiveData {
        subscription {
            appSettings
                .useMeteredNetwork
                .subscribe { useMeteredNetworkSelected.value = it }
        }
    }

    override fun onSourceRefreshMinIntervalSelected(interval: SourceRefreshInterval) {
        subscription {
            appSettings
                .sourceRefreshMinInterval
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
                .subscribe { }
        }
    }

    override fun onArticleListImagesChanged(isActive: Boolean) =
        appSettings.setArticlesListImagesEnabled(isActive)

    override fun onUseMeteredNetworkChanged(isActive: Boolean) =
        appSettings.setUseMeteredNetwork(isActive)
}
