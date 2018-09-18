package com.lelloman.read.testutils

import android.content.Context
import android.support.test.espresso.UiController
import android.support.test.espresso.ViewAction
import android.view.View
import android.widget.Spinner
import com.lelloman.common.di.BaseApplicationModule
import com.lelloman.common.view.MeteredConnectionChecker
import com.lelloman.read.http.HttpModule
import com.lelloman.read.widget.ToggleSettingItemView
import com.lelloman.testutils.rotateNatural
import org.hamcrest.BaseMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher
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

fun setSpinnerItem(name: String) = object : ViewAction {
    override fun getDescription() = "setSpinnerItem($name)"

    override fun getConstraints(): Matcher<View>? = object : BaseMatcher<View>() {
        override fun describeTo(description: Description?) = Unit

        override fun matches(item: Any?) = item is Spinner
    }

    override fun perform(uiController: UiController?, view: View?) {
//        view?.performClick()
//        clickViewWithText(name)
//
//        onView(withId(spinnerId)).perform(click())
//        onData(allOf(`is`(instanceOf(String::class.java)), `is`(selectionText))).perform(click())
    }
}

fun setUpTestAppWithMockedHttpStack(
    isMeteredNetworkValueProvider: () -> Boolean
) {
    rotateNatural()
    TestApp.dependenciesUpdate {
        it.httpModule = object : HttpModule() {
            override fun provideOkHttpClient() = MockHttpClient()
        }
        it.baseApplicationModule = object : BaseApplicationModule(it) {
            override fun provideMeteredConnectionChecker(context: Context): MeteredConnectionChecker {
                return object : MeteredConnectionChecker {
                    override fun isNetworkMetered() = isMeteredNetworkValueProvider.invoke()
                }
            }
        }
    }
    TestApp.resetPersistence()
}