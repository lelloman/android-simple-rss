package com.lelloman.simplerss.testutils

import android.content.Context
import android.view.View
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import com.lelloman.common.di.BaseApplicationModule
import com.lelloman.common.http.HttpModule
import com.lelloman.common.view.MeteredConnectionChecker
import com.lelloman.instrumentedtestutils.Screen
import com.lelloman.instrumentedtestutils.ViewActions
import com.lelloman.instrumentedtestutils.rotateNatural
import com.lelloman.simplerss.widget.ToggleSettingItemView
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

@Deprecated("move this to common")
fun <T : Screen> T.clickOnOk(): T = apply {
    ViewActions.clickViewWithText("OK")
}