package com.lelloman.read.core.navigation

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class DeepLinkTest {

    @Test
    fun hasStringRepresentation1() {
        val deepLink = DeepLink(NavigationScreen.ADD_SOURCE, mapOf(
            "asdasd" to 123,
            "muuh" to 1.0,
            "cracra" to false,
            "sta" to "ceppa"
        ))

        assertThat(deepLink.toString()).isEqualTo("smurfs:/ADD_SOURCE?asdasd=123&cracra=false&muuh=1.0&sta=ceppa")
    }
}