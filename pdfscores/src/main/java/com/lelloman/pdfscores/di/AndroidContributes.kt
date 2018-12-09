package com.lelloman.pdfscores.di

import com.lelloman.common.view.InjectableActivity
import com.lelloman.pdfscores.publicapi.PublicApiContentProvider
import com.lelloman.pdfscores.ui.collectionapps.CollectionAppsActivity
import com.lelloman.pdfscores.ui.pdfviewer.PdfViewerActivity
import com.lelloman.pdfscores.ui.pdfscoreslist.view.PdfScoresListActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
interface AndroidContributes {

    @ContributesAndroidInjector
    fun contributePublicApiContentProvider(): PublicApiContentProvider

    @ContributesAndroidInjector
    fun contributeBaseActivity(): InjectableActivity

    @ContributesAndroidInjector
    fun contributePdfScoresListActivity(): PdfScoresListActivity

    @ContributesAndroidInjector
    fun contributePdfViewerActivity(): PdfViewerActivity

    @ContributesAndroidInjector
    fun contributeCollectionAppsActivity(): CollectionAppsActivity
}