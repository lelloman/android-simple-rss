package com.lelloman.deviceinfo.ui

import android.os.Build
import android.text.Html
import android.text.SpannableString
import android.text.Spanned
import com.lelloman.common.view.ResourceProvider
import com.lelloman.common.viewmodel.BaseListItemViewModel
import com.lelloman.deviceinfo.R
import com.lelloman.deviceinfo.infoitem.DisplayInfoItem

class DisplayInfoItemListItemViewModel(
    private val resourceProvider: ResourceProvider
) : InfoItemListItemViewModel<DisplayInfoItem>{

    private val emptySpanned = SpannableString("")

    var textHtml: Spanned = emptySpanned
        private set

    override fun bind(item: DisplayInfoItem) {
        textHtml = item.toHtmlString()
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

    private fun fromHtml(text: String): Spanned = if (Build.VERSION.SDK_INT >= 24) {
        Html.fromHtml(text, 0)
    } else {
        Html.fromHtml(text)
    }
}