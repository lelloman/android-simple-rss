package com.lelloman.launcher.classification

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class PackageLaunchEncoderTest {

    @Test
    fun `encode and decodes`() {
        val identifiers = (0 until 10).map(Int::toString)

        val tested = IdentifierEncoder(identifiers)

        val encoded = identifiers.map { tested.encode(it) }
        val decoded = encoded.map(tested::decode)

        decoded.forEachIndexed { index, it ->
            assertThat(it).isEqualTo(identifiers[index])
        }
    }

    @Test
    fun `encode and decodes with duplicates`() {
        val identifiers = (0 until 10).map(Int::toString)
            .let { it.plus(ArrayList(it)) }

        val tested = IdentifierEncoder(identifiers)

        val encoded = identifiers.map { tested.encode(it) }
        val decoded = encoded.map(tested::decode)

        decoded.forEachIndexed { index, it ->
            assertThat(it).isEqualTo(identifiers[index])
        }
    }
}