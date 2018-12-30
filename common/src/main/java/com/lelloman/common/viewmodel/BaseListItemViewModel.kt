package com.lelloman.common.viewmodel

import com.lelloman.common.utils.model.ModelWithId

interface BaseListItemViewModel<ID, M : ModelWithId<ID>> {
    fun bind(item: M)
}