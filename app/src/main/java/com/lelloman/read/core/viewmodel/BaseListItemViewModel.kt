package com.lelloman.read.core.viewmodel

import com.lelloman.common.utils.ModelWithId

interface BaseListItemViewModel<M : ModelWithId> {
    fun bind(item: M)
}