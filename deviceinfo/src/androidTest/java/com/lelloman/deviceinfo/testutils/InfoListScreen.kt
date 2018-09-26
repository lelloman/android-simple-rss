package com.lelloman.deviceinfo.testutils

import android.text.Html
import com.lelloman.common.utils.model.Resolution
import com.lelloman.deviceinfo.R
import com.lelloman.testutils.Screen
import com.lelloman.testutils.clickOnRecyclerViewItem
import com.lelloman.testutils.viewWithTextIsDisplayed

class InfoListScreen : Screen() {

    init {
        viewVisible(R.id.recycler_view_info_list)
    }

    fun showsDensityDpi(dpi: Int) = apply {
        viewWithTextIsDisplayed(
            stripHtml(string(R.string.screen_density_dpi, dpi))
        )
    }

    fun showsResolutionPx(resolution: Resolution) = apply {
        viewWithTextIsDisplayed(
            stripHtml(string(R.string.screen_resolution_px, resolution.width, resolution.height)))
    }

    fun showsResolutionDp(resolution: Resolution) = apply {
        viewWithTextIsDisplayed(
            stripHtml(string(R.string.screen_resolution_dp, resolution.width, resolution.height)))
    }

    fun clickOnItem(position: Int) = apply {
        clickOnRecyclerViewItem(position, R.id.recycler_view_info_list)
    }

    private fun stripHtml(string: String): String {
        return Html.fromHtml(string).toString()
    }

    fun showsToast(message: String) = apply {
        viewWithTextIsDisplayed(message)
    }
}