package com.lelloman.simplerss.ui.settings.viewmodel

import androidx.databinding.Observable
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.lelloman.common.utils.LazyLiveData
import com.lelloman.common.view.AppTheme
import com.lelloman.common.view.FileProvider
import com.lelloman.common.view.SemanticTimeProvider
import com.lelloman.simplerss.persistence.db.AppDatabase
import com.lelloman.simplerss.persistence.settings.AppSettings
import com.lelloman.simplerss.persistence.settings.SourceRefreshInterval
import com.lelloman.simplerss.ui.OpenClearDataConfirmationScreenCommand
import io.reactivex.Completable

class SettingsViewModelImpl(
    private val appSettings: AppSettings,
    semanticTimeProvider: SemanticTimeProvider,
    private val appDatabase: AppDatabase,
    private val fileProvider: FileProvider,
    dependencies: Dependencies
) : SettingsViewModel(dependencies) {

    private val logger = dependencies.loggerFactory.getLogger(javaClass)

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

    override val themes: MutableLiveData<List<String>> by LazyLiveData {
        subscription {
            appSettings.appTheme
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
                .subscribe { selectedTheme.set(AppTheme.values().indexOf(it)) }
        }
        themes.value = AppTheme.values().map { it.name }
    }
    override val selectedTheme = ObservableField<Int>().also {
        it.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                if (sender is ObservableField<*>) {
                    (sender.get() as? Int)?.let { position ->
                        appSettings.setAppTheme(AppTheme.values()[position])
                    }
                }
            }
        })
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

    override val openArticlesInAppSelected: MutableLiveData<Boolean> by LazyLiveData {
        subscription {
            appSettings
                .openArticlesInApp
                .subscribe { openArticlesInAppSelected.value = it }
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

    override fun onOpenArticlesInAppChanged(isActive: Boolean) =
        appSettings.setOpenArticlesInApp(isActive)

    override fun onClearDataClicked() = emitCommand(OpenClearDataConfirmationScreenCommand)

    override fun onClearDataConfirmed() {
        Completable
            .fromAction { appDatabase.clearAllTables() }
            .onErrorComplete {
                logger.e("Something went wrong when trying to clear db", it)
                true
            }
            .andThen(fileProvider.deleteAllCacheFiles())
            .doOnError { logger.e("Something went wrong when trying to delete cache files", it) }
            .onErrorComplete()
            .andThen(fileProvider.deleteAllInternalFiles())
            .doOnError { logger.e("Something went wrong when trying to delete internal files", it) }
            .onErrorComplete()
            .subscribeOn(ioScheduler)
            .subscribe()
    }
}
