package com.lelloman.common.viewmodel

import com.lelloman.common.utils.model.ModelWithId

interface BaseListItemViewModel<M : ModelWithId> {
    fun bind(item: M)
}