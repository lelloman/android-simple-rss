package com.lelloman.deviceinfo.ui.viewmodel

import com.lelloman.common.viewmodel.BaseListItemViewModel
import com.lelloman.deviceinfo.infoitem.InfoItem

interface InfoItemListItemViewModel<T : InfoItem> : BaseListItemViewModel<Long, T>