package com.lelloman.read.testutils

import android.support.test.espresso.UiController
import android.support.test.espresso.ViewAction
import android.view.View
import com.lelloman.read.widget.ToggleSettingItemView
import org.hamcrest.BaseMatcher
import org.hamcrest.Description
import org.mockito.Mockito.`when`
import org.mockito.stubbing.OngoingStubbing


fun <T> whenever(methodCall: T): OngoingStubbing<T> = `when`(methodCall)




fun setToggleSettingChecked(isChecked: Boolean) = object : ViewAction {
    override fun getDescription(): String = "setChecked($isChecked)"

    override fun getConstraints() = object : BaseMatcher<View>() {
        override fun describeTo(description: Description?) = Unit

        override fun matches(item: Any?) = item is ToggleSettingItemView
    }

    override fun perform(uiController: UiController?, view: View?) {
        (view as ToggleSettingItemView).setChecked(isChecked)
    }
}
