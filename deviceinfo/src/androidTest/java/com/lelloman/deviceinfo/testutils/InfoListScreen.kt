package com.lelloman.deviceinfo.testutils

import android.text.Html
import com.lelloman.common.utils.model.Resolution
import com.lelloman.deviceinfo.R
import com.lelloman.deviceinfo.device.NetworkInterface
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

    fun showsNetworkInterface(networkInterface: NetworkInterface) = apply {
        viewWithTextIsDisplayed(networkInterface.name)
        viewWithTextIsDisplayed(networkInterface.hwAddress)
        val netAddresses = networkInterface.netAddresses.let { addresses ->
            if (addresses.isEmpty()) {
                "-"
            } else {
                addresses.joinToString("\n")
            }
        }
        viewWithTextIsDisplayed(netAddresses)
    }

    private fun stripHtml(string: String): String {
        return Html.fromHtml(string).toString()
    }

    fun showsToast(message: String) = apply {
        viewWithTextIsDisplayed(message)
    }
}