package com.lelloman.launcher.ui.launches.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.net.Uri
import com.lelloman.common.logger.LoggerFactory
import com.lelloman.common.utils.LazyLiveData
import com.lelloman.common.view.ContentUriOpener
import com.lelloman.common.view.actionevent.PickFileActionEvent
import com.lelloman.common.view.actionevent.ShareFileViewActionEvent
import com.lelloman.common.viewmodel.BaseViewModel
import com.lelloman.launcher.R
import com.lelloman.launcher.packages.PackageLaunchesExporter
import com.lelloman.launcher.packages.PackagesManager
import com.lelloman.launcher.persistence.PackageLaunchDao
import com.lelloman.launcher.ui.launches.PackageLaunchListItem
import io.reactivex.Completable
import io.reactivex.Scheduler
import java.io.BufferedReader
import java.io.InputStreamReader

class LaunchesViewModelImpl(
    private val ioScheduler: Scheduler,
    dependencies: BaseViewModel.Dependencies,
    private val packageLaunchDao: PackageLaunchDao,
    private val packagesManager: PackagesManager,
    loggerFactory: LoggerFactory,
    private val packageLaunchesExporter: PackageLaunchesExporter,
    private val contentUriOpener: ContentUriOpener
) : LaunchesViewModel(dependencies) {

    private var exportingLaunches = false
    private val logger = loggerFactory.getLogger(LaunchesViewModelImpl::class.java)

    override val isLoading: MutableLiveData<Boolean> by LazyLiveData {
        subscription {
            packagesManager
                .updatingPackages
                .subscribeOn(ioScheduler)
                .observeOn(ioScheduler)
                .subscribe {
                    isLoading.postValue(it)
                }
        }
    }

    override val launches: MutableLiveData<List<PackageLaunchListItem>> by LazyLiveData {
        subscription {
            packageLaunchDao
                .getAll()
                .map { launches ->
                    launches.map {
                        PackageLaunchListItem(
                            packageLaunch = it,
                            icon = packagesManager.getIconForPackageLaunch(it)
                        )
                    }
                }
                .subscribeOn(ioScheduler)
                .observeOn(ioScheduler)
                .subscribe {
                    launches.postValue(it)
                }
        }
    }

    override fun onImportClicked() {
        viewActionEvents.postValue(PickFileActionEvent(IMPORT_FILE_REQUEST_CODE))
    }

    override fun onRefreshClicked() {
        packagesManager.updateInstalledPackages()
    }

    override fun onContentPicked(uri: Uri, requestCode: Int) {
        if (requestCode == IMPORT_FILE_REQUEST_CODE) {
            contentUriOpener.open(uri)?.let { inputStream ->
                val bufferedReader = BufferedReader(InputStreamReader(inputStream))
                packageLaunchesExporter
                    .readExportFile(bufferedReader)
                    .flatMapCompletable {
                        Completable.fromAction {
                            packageLaunchDao.insert(it)
                        }
                    }
                    .subscribeOn(ioScheduler)
                    .observeOn(ioScheduler)
                    .subscribe({

                    }, {
                        logger.w("Error while reading export uri $uri")
                        shortToast(getString(R.string.read_export_file_failed))
                    })
            } ?: logger.w("Couldn't open content uri $uri")
        }
    }

    override fun onExportClicked() {
        if (exportingLaunches) {
            return
        }
        exportingLaunches = true

        val launchesList = launches.value!!.map { it.packageLaunch }
        val fileName = "launches.csv"
        subscription {
            packageLaunchesExporter
                .createExportFile(fileName, launchesList)
                .subscribeOn(ioScheduler)
                .observeOn(ioScheduler)
                .doAfterTerminate { exportingLaunches = false }
                .subscribe(
                    {
                        viewActionEvents.postValue(ShareFileViewActionEvent(
                            file = it,
                            authority = getString(R.string.file_share_authority)
                        ))
                    },
                    { throwable ->
                        logger.e("Error while exporting launches list", throwable)
                    }
                )
        }
    }

    private companion object {
        const val IMPORT_FILE_REQUEST_CODE = 123
    }
}