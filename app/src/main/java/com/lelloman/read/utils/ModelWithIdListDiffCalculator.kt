package com.lelloman.read.utils

import android.support.v7.util.DiffUtil
import com.lelloman.read.core.ModelWithId

class ModelWithIdListDiffCalculator : DiffUtil.Callback() {

    private var oldList = emptyList<ModelWithId>()
    private var newList = emptyList<ModelWithId>()

    fun computeDiff(oldList: List<ModelWithId>, newList: List<ModelWithId>): DiffUtil.DiffResult {
        this.oldList = oldList
        this.newList = newList
        return DiffUtil.calculateDiff(this)
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition].id == newList[newItemPosition].id

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition].hashCode() == newList[newItemPosition].hashCode()
}