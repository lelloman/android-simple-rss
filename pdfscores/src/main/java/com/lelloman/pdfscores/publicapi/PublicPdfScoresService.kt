package com.lelloman.pdfscores.publicapi

import android.annotation.SuppressLint
import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import com.lelloman.pdfscores.IPublicPdfScoresService
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

@SuppressLint("Registered")
class PublicPdfScoresService : Service() {

    private val binder = object : IPublicPdfScoresService.Stub() {
        override fun getVersion(): Int = PublicApiConstants.VERSION

        override fun getPackagename() = packageName
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