package com.lelloman.deviceinfo.ui

import android.os.Build
import android.text.Html
import android.text.SpannableString
import android.text.Spanned
import com.lelloman.common.view.ResourceProvider
import com.lelloman.common.viewmodel.BaseListItemViewModel
import com.lelloman.deviceinfo.R
import com.lelloman.deviceinfo.device.NetworkInterface
import com.lelloman.deviceinfo.infoitem.DisplayInfoItem
import com.lelloman.deviceinfo.infoitem.InfoItem
import com.lelloman.deviceinfo.infoitem.NetworkInfoItem

class InfoItemListItemViewModel(
    private val resourceProvider: ResourceProvider
) : BaseListItemViewModel<InfoItem> {

    private val emptySpanned = SpannableString("")

    var textHtml: Spanned = emptySpanned
        private set

    override fun bind(item: InfoItem) {
        textHtml = toHtmlString(item)
    }

    private fun toHtmlString(infoItem: InfoItem): Spanned = when (infoItem) {
        is DisplayInfoItem -> infoItem.toHtmlString()
        is NetworkInfoItem -> infoItem.toHtmlString()
        else -> emptySpanned
    }

    private fun DisplayInfoItem.toHtmlString(): Spanned {
        val resolutionPxString = resourceProvider.getString(R.string.screen_resolution_px, this.screenResolutionPx.width, this.screenResolutionPx.height)
        val resolutionDpString = resourceProvider.getString(R.string.screen_resolution_dp, this.screenResolutionDp.width, this.screenResolutionDp.height)
        val densityDpiString = resourceProvider.getString(R.string.screen_density_dpi, this.screenDensityDpi)
        return fromHtml("""$resolutionPxString<br>
        $resolutionDpString<br>
        $densityDpiString
        """.trimIndent())
    }

    private fun NetworkInfoItem.toHtmlString(): Spanned {
        return fromHtml("""
            ${this.networkInterfaces.joinToString("<br>") { networkInterfaceString(it) }}
        """.trimIndent())
    }

    private fun networkInterfaceString(networkInterface: NetworkInterface): String {
        val netAddresses = networkInterface
            .netAddresses
            .asSequence()
            .mapIndexed { index, address ->
                val spacing = "&emsp;${if (index == 0) "" else "&emsp;&emsp;"}"
                "$spacing$address"
            }
            .joinToString("<br>")
        return "<b>${networkInterface.name}</b><br>&emsp;hw: <b>${networkInterface.hwAddress}</b><br>&emsp;net:<b>$netAddresses</b>"
    }

    private fun fromHtml(text: String): Spanned = if (Build.VERSION.SDK_INT >= 24) {
        Html.fromHtml(text, 0)
    } else {
        Html.fromHtml(text)
    }
}