package com.lelloman.read.core

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ActionTokenProviderTest {

    private val tested = ActionTokenProvider()

    @Test
    fun `generates unique tokens`(){
        val tokens = hashSetOf<String>()

        for (i in 0 until 10_000){
            val token = tested.makeActionToken()
            assertThat(tokens).doesNotContain(token)
            tokens.add(token)
        }
    }
}