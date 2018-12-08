package com.lelloman.pdfscores.publicapi

import android.annotation.SuppressLint
import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import com.lelloman.common.di.qualifiers.NewThreadScheduler
import com.lelloman.pdfscores.IPublicPdfScoresService
import com.lelloman.pdfscores.persistence.PdfScoresRepository
import dagger.android.AndroidInjection
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

@SuppressLint("Registered")
class PublicPdfScoresService : Service() {

    @Inject
    @field:NewThreadScheduler
    lateinit var newThreadScheduler: Scheduler

    @Inject
    lateinit var pdfScoresRepository: PdfScoresRepository

    private val binder = object : IPublicPdfScoresService.Stub() {
        override fun getVersion(): Int = PublicApiConstants.VERSION

        override fun getPackagename() = packageName
    }

    override fun onCreate() {
        super.onCreate()
        AndroidInjection.inject(this)
    }

    override fun onBind(intent: Intent?) = binder
}

class PublicPdfScoresServiceConnection : ServiceConnection {

    private val boundSubject = BehaviorSubject.create<Boolean>().apply { onNext(false) }
    val bound: Observable<Boolean> = boundSubject.hide()

    private val serviceSubject = BehaviorSubject.create<IPublicPdfScoresService>()
    val service: Observable<IPublicPdfScoresService> = serviceSubject.hide()

    override fun onServiceDisconnected(name: ComponentName?) {
        boundSubject.onNext(false)
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        serviceSubject.onNext(IPublicPdfScoresService.Stub.asInterface(service))
        boundSubject.onNext(true)
    }
}