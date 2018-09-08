package com.lelloman.read.core

import com.lelloman.read.mock.MockAppSettings
import com.lelloman.read.mock.MockMeteredConnectionChecker
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.verify
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.RequestCreator
import io.reactivex.Observable
import org.junit.Test

class PicassoWrapTest {

    private val appSettings = MockAppSettings()
    private val meteredConnectionChecker = MockMeteredConnectionChecker()
    private val picassoRequestCreator: RequestCreator = mock {
        on { networkPolicy(any()) }.thenAnswer { it.mock }
    }

    private val tested = PicassoWrapImpl(
        appSettings = appSettings,
        meteredConnectionChecker = meteredConnectionChecker,
        requestCreatorProvider = { picassoRequestCreator }
    )

    @Test
    fun `does not set offline policy if used metered network is true`() {
        givenUseMeteredNetworkIsTrue()

        tested.loadUrlIntoImageView(mock(), mock(), null)

        verify(picassoRequestCreator, never()).networkPolicy(NetworkPolicy.OFFLINE)
    }

    @Test
    fun `does set offline policy if cannot use metered network and network is metered`() {
        givenUseMeteredNetworkIsFalse()
        givenNetworkIsMetered()

        tested.loadUrlIntoImageView(mock(), mock(), null)

        verify(picassoRequestCreator).networkPolicy(NetworkPolicy.OFFLINE)
    }

    @Test
    fun `does not set offline policy if cannot use metered network and network is not metered`() {
        givenUseMeteredNetworkIsFalse()
        givenNetworkIsNotMetered()

        tested.loadUrlIntoImageView(mock(), mock(), null)

        verify(picassoRequestCreator, never()).networkPolicy(NetworkPolicy.OFFLINE)
    }

    @Test
    fun `does not set placeholder if argument is null`() {
        givenUseMeteredNetworkIsTrue()

        tested.loadUrlIntoImageView(mock(), mock(), null)

        verify(picassoRequestCreator, never()).placeholder(any<Int>())
    }

    @Test
    fun `sets placeholder if argument is passed`() {
        givenUseMeteredNetworkIsTrue()
        val placeHolderId = 1234

        tested.loadUrlIntoImageView(mock(), mock(), placeHolderId)

        verify(picassoRequestCreator).placeholder(placeHolderId)
    }

    private fun givenUseMeteredNetworkIsTrue() {
        appSettings.providedUseMeteredNetwork = Observable.just(true)
    }

    private fun givenUseMeteredNetworkIsFalse() {
        appSettings.providedUseMeteredNetwork = Observable.just(false)
    }

    private fun givenNetworkIsMetered() {
        meteredConnectionChecker.isNetworkMeteredValue = true
    }

    private fun givenNetworkIsNotMetered() {
        meteredConnectionChecker.isNetworkMeteredValue = false
    }
}