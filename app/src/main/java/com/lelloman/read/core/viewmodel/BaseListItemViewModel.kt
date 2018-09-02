package com.lelloman.read.core.viewmodel

import com.lelloman.read.core.ModelWithId

interface BaseListItemViewModel<M : ModelWithId> {
    fun bind(item: M)
}