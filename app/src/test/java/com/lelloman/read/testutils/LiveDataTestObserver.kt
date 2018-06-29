package com.lelloman.read.testutils

import android.arch.lifecycle.Observer
import com.google.common.truth.Truth.assertThat

class LiveDataTestObserver<T> : Observer<T> {

    private val mutableValues = mutableListOf<T?>()
    val values = mutableValues as List<T?>

    override fun onChanged(value: T?) {
        mutableValues.add(value)
    }

    fun assertValues(vararg expectedValue: T?) {
        assertThat(values).isEqualTo(expectedValue.toList())
    }

    fun assertNoValues() {
        assertThat(values).isEmpty()
    }
}