package com.lelloman.launcher.classification

import com.lelloman.launcher.persistence.model.PackageLaunch
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class PackageLaunchEncoderTest {

    @Test
    fun `encode and decodes`() {
        val launches = (0 until 10).map(::pkgLaunch)

        val tested = PackageLaunchEncoder(launches)

        val encoded = launches.map(tested::encode)
        val decoded = encoded.map(tested::decode)

        decoded.forEachIndexed { index, it ->
            assertThat(it).isEqualTo(launches[index].identifier())
        }
    }

    @Test
    fun `encode and decodes with duplicates`() {
        val launches = (0 until 10).map(::pkgLaunch)
            .let { it.plus(ArrayList(it)) }

        val tested = PackageLaunchEncoder(launches)

        val encoded = launches.map(tested::encode)
        val decoded = encoded.map(tested::decode)

        decoded.forEachIndexed { index, it ->
            assertThat(it).isEqualTo(launches[index].identifier())
        }
    }

    private fun pkgLaunch(index: Int) = PackageLaunch(
        id = index.toLong(),
        timestampUtc = index.toLong(),
        packageName = index.toString(),
        activityName = index.toString()
    )
}