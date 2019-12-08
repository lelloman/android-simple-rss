package com.lelloman.simplerss.testutils

import android.content.Context
import android.view.View
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import com.lelloman.common.androidtestutils.rotateNatural
import com.lelloman.common.di.BaseApplicationModuleFactory
import com.lelloman.common.http.HttpModuleFactory
import com.lelloman.common.view.MeteredConnectionChecker
import com.lelloman.simplerss.widget.ToggleSettingItemView
import okhttp3.CookieJar
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
        it.httpModuleFactory = object : HttpModuleFactory() {
            override fun provideOkHttpClient(cookieJar: CookieJar) = MockHttpClient()
        }

        it.baseApplicationModuleFactory = object : BaseApplicationModuleFactory() {
            override fun provideMeteredConnectionChecker(context: Context): MeteredConnectionChecker {
                return object : MeteredConnectionChecker {
                    override fun isNetworkMetered() = isMeteredNetworkValueProvider.invoke()
                }
            }
        }
    }
    TestApp.resetPersistence()
}