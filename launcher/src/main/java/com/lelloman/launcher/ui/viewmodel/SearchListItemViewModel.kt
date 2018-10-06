package com.lelloman.launcher.ui.viewmodel

import android.databinding.Observable
import android.databinding.ObservableField
import com.lelloman.common.viewmodel.BaseListItemViewModel

class SearchListItemViewModel(
    onSearchQueryChanged: (String) -> Unit
) : BaseListItemViewModel<SearchDrawerListItem> {

    val appsSearchQuery = ObservableField<String>().apply {
        addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback(){
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                onSearchQueryChanged.invoke(this@apply.get()!!)
            }
        })
    }

    override fun bind(item: SearchDrawerListItem) {

    }
}