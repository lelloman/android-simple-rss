package com.lelloman.deviceinfo.ui.viewmodel

import android.os.Build
import android.text.Html
import android.text.SpannableString
import android.text.Spanned
import com.lelloman.common.view.ResourceProvider
import com.lelloman.common.viewmodel.BaseListItemViewModel
import com.lelloman.deviceinfo.R
import com.lelloman.deviceinfo.infoitem.DisplayInfoItem
import com.lelloman.deviceinfo.infoitem.InfoItem

class DisplayInfoItemListItemViewModel(
    private val resourceProvider: ResourceProvider,
    private val onDisplayButtonClickListener: ((InfoItem) -> Unit)?
) : BaseListItemViewModel<Long, DisplayInfoItem> {

    private val emptySpanned = SpannableString("")
    private lateinit var infoItem: DisplayInfoItem

    var resolutionPx: Spanned = emptySpanned
        private set

    var resolutionDp: Spanned = emptySpanned
        private set

    var densityDpi: Spanned = emptySpanned
        private set

    fun onDisplayButtonClicked() = onDisplayButtonClickListener?.invoke(infoItem)

    override fun bind(item: DisplayInfoItem) {
        this.infoItem = item
        resolutionPx = fromHtml(resourceProvider.getString(R.string.screen_resolution_px, item.screenResolutionPx.width, item.screenResolutionPx.height))
        resolutionDp = fromHtml(resourceProvider.getString(R.string.screen_resolution_dp, item.screenResolutionDp.width, item.screenResolutionDp.height))
        densityDpi = fromHtml(resourceProvider.getString(R.string.screen_density_dpi, item.screenDensityDpi))
    }

    private fun fromHtml(text: String): Spanned = if (Build.VERSION.SDK_INT >= 24) {
        Html.fromHtml(text, 0)
    } else {
        Html.fromHtml(text)
    }
}