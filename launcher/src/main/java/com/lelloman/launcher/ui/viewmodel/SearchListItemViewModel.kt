package com.lelloman.launcher.ui.viewmodel

import android.databinding.Observable
import android.databinding.ObservableField
import android.util.Log
import com.lelloman.common.viewmodel.BaseListItemViewModel

class SearchListItemViewModel(
    onAppsQuerySearchChangeListener: (String) -> Unit
) : BaseListItemViewModel<SearchDrawerListItem> {

    val appsSearchQuery = ObservableField<String>().apply {
        addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback(){
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                onAppsQuerySearchChangeListener.invoke(this@apply.get()!!)
            }
        })
    }

    override fun bind(item: SearchDrawerListItem) {

    }
}