package com.lelloman.read.core.navigation

import com.google.common.truth.Truth.assertThat
import com.lelloman.common.navigation.DeepLink
import com.lelloman.read.navigation.ReadNavigationScreen
import org.junit.Test

class DeepLinkTest {

    @Test
    fun hasStringRepresentation1() {
        val deepLink = DeepLink(ReadNavigationScreen.ADD_SOURCE)
            .putInt("asdasd", 123)
            .putDouble("muuh", 1.0)
            .putBoolean("cracra", false)
            .putString("sta", "ceppa")

        assertThat(deepLink.toString()).isEqualTo("smurfs:/ADD_SOURCE?asdasd=123&cracra=false&muuh=1.0&sta=ceppa")
    }
}