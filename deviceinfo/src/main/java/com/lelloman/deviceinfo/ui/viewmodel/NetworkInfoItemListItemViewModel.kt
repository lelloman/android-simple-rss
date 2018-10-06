package com.lelloman.deviceinfo.ui.viewmodel

import android.os.Build
import android.text.Html
import android.text.SpannableString
import android.text.Spanned
import com.lelloman.deviceinfo.device.NetworkInterface
import com.lelloman.deviceinfo.infoitem.NetworkInfoItem

class NetworkInfoItemListItemViewModel : InfoItemListItemViewModel<NetworkInfoItem> {

    private val emptySpanned = SpannableString("")

    @Suppress("MemberVisibilityCanBePrivate")
    var textHtml: Spanned = emptySpanned
        private set

    override fun bind(item: NetworkInfoItem) {
        textHtml = item.toHtmlString()
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