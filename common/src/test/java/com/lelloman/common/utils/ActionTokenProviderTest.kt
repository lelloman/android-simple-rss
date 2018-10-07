package com.lelloman.common.utils

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class ActionTokenProviderTest {

    private val tested = ActionTokenProvider()

    @Test
    fun `generates unique tokens`(){
        val tokens = hashSetOf<String>()

        for (i in 0 until 1000) {
            val token = tested.makeActionToken()
            assertThat(tokens).doesNotContain(token)
            tokens.add(token)
        }
    }
}