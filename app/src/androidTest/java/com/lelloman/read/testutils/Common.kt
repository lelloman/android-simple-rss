package com.lelloman.read.testutils

import android.content.Context
import android.support.test.espresso.UiController
import android.support.test.espresso.ViewAction
import android.view.View
import com.lelloman.common.di.BaseApplicationModule
import com.lelloman.common.view.MeteredConnectionChecker
import com.lelloman.read.http.HttpModule
import com.lelloman.read.widget.ToggleSettingItemView
import com.lelloman.testutils.rotateNatural
import org.hamcrest.BaseMatcher
import org.hamcrest.Description

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