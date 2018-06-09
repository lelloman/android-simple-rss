package com.lelloman.read.utils

import android.support.v7.util.DiffUtil
import com.lelloman.read.articleslist.model.Article

class ArticleListDiffCalculator : DiffUtil.Callback() {

    private var oldList = emptyList<Article>()
    private var newList = emptyList<Article>()

    fun computeDiff(oldList: List<Article>, newList: List<Article>): DiffUtil.DiffResult {
        this.oldList = oldList
        this.newList = newList
        return DiffUtil.calculateDiff(this)
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition].id == newList[newItemPosition].id

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition] == newList[newItemPosition]
}