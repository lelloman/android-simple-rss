package com.lelloman.pdfscores

import android.app.Activity
import android.util.Log
import com.lelloman.common.BaseApplication
import com.lelloman.common.di.BaseApplicationModule
import com.lelloman.pdfscores.di.DaggerAppComponent
import com.lelloman.pdfscores.persistence.Author
import com.lelloman.pdfscores.persistence.AuthorsDao
import com.lelloman.pdfscores.persistence.PdfScoreModel
import com.lelloman.pdfscores.persistence.PdfScoresDao
import com.lelloman.pdfscores.publicapi.PublicPdfScoresAppsFinder
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

open class PdfScoresApplication : BaseApplication(), HasActivityInjector {

    @Inject
    lateinit var dispatchingActivityAndroidInjector: DispatchingAndroidInjector<Activity>

    @Inject
    lateinit var pdfScoresDao: PdfScoresDao

    @Inject
    lateinit var authorsDao: AuthorsDao

    @Inject
    lateinit var appsFinder: PublicPdfScoresAppsFinder

    override fun inject() = DaggerAppComponent
        .builder()
        .baseApplicationModule(BaseApplicationModule(this))
        .build()
        .inject(this)

    override fun activityInjector() = dispatchingActivityAndroidInjector

    private fun insertTestDataIntoDb() = Completable.fromAction {
        val bach = Author(
            firstName = "Giovanni",
            lastName = "Bach"
        )
        val bachId = authorsDao.insert(bach)[0]

        val scores = arrayOf(
            PdfScoreModel(
                uri = "asset:///bach_clavicembalo_ben_temperato.pdf",
                created = System.currentTimeMillis(),
                lastOpened = System.currentTimeMillis(),
                title = "Clavicembalo ben temperato",
                authorId = bachId
            )
        )

        pdfScoresDao.insert(*scores)
    }

    override fun onCreate() {
        super.onCreate()

        val unused = appsFinder
            .pdfScoresApps
            .subscribeOn(Schedulers.io())
            .subscribe {
                it.forEach {
                    Log.d("ASDASD", "${it.packageName} ${it.publicApiVersion}")
                }
            }

        Completable
            .fromAction(authorsDao::deleteAll)
            .andThen(insertTestDataIntoDb())
            .subscribeOn(Schedulers.io())
            .blockingAwait()
    }
}