package com.lelloman.pdfscores.di

import com.lelloman.common.view.InjectableActivity
import com.lelloman.pdfscores.publicapi.PublicApiContentProvider
import com.lelloman.pdfscores.ui.pdfviewer.PdfViewerActivity
import com.lelloman.pdfscores.ui.recentscores.view.RecentScoresActivity
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
    fun contributeRecentScoresActivity(): RecentScoresActivity

    @ContributesAndroidInjector
    fun contributePdfViewerActivity(): PdfViewerActivity
}